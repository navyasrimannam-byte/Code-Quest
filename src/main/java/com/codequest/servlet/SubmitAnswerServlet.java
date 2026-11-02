package com.codequest.servlet;

import com.codequest.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

@WebServlet("/submit-answer")
public class SubmitAnswerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        long userId = (Long) session.getAttribute("userId");
        String answer = request.getParameter("answer");
        String missionIdText = request.getParameter("missionId");
        long missionId;
        if (answer == null || !answer.matches("(?i)[ABCD]") || missionIdText == null
                || !missionIdText.matches("[0-9]{1,18}")) {
            showResult(request, response, false, "Choose one answer before submitting.", 0);
            return;
        }
        missionId = Long.parseLong(missionIdText);
        answer = answer.toUpperCase(Locale.ROOT);
        boolean correct;
        int reward;

        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            connection.setAutoCommit(false);
            try {
                int currentXp;
                try (PreparedStatement userLock = connection.prepareStatement(
                        "SELECT xp FROM users WHERE id = ? FOR UPDATE")) {
                    userLock.setLong(1, userId);
                    try (ResultSet user = userLock.executeQuery()) {
                        if (!user.next()) {
                            connection.rollback();
                            showResult(request, response, false, "Your account could not be found. Please sign in again.", 0);
                            return;
                        }
                        currentXp = user.getInt("xp");
                    }
                }

                String correctAnswer;
                try (PreparedStatement missionQuery = connection.prepareStatement(
                        "SELECT correct_answer, xp_reward FROM missions WHERE id = ? AND active = TRUE")) {
                    missionQuery.setLong(1, missionId);
                    try (ResultSet mission = missionQuery.executeQuery()) {
                        if (!mission.next()) {
                            connection.rollback();
                            showResult(request, response, false, "That mission is no longer available.", 0);
                            return;
                        }
                        correctAnswer = mission.getString("correct_answer");
                        reward = mission.getInt("xp_reward");
                    }
                }

                correct = answer.equals(correctAnswer);
                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO mission_submissions (user_id, mission_id, selected_answer, is_correct) "
                                + "VALUES (?, ?, ?, ?)")) {
                    insert.setLong(1, userId);
                    insert.setLong(2, missionId);
                    insert.setString(3, answer);
                    insert.setBoolean(4, correct);
                    insert.executeUpdate();
                }

                if (correct) {
                    int newXp = Math.addExact(currentXp, reward);
                    int newLevel = newXp / 100 + 1;
                    try (PreparedStatement update = connection.prepareStatement(
                            "UPDATE users SET xp = ?, level = ? WHERE id = ?")) {
                        update.setInt(1, newXp);
                        update.setInt(2, newLevel);
                        update.setLong(3, userId);
                        update.executeUpdate();
                    }
                }
                connection.commit();
            } catch (SQLException | ArithmeticException exception) {
                connection.rollback();
                if (exception instanceof SQLException sqlException && isDuplicateSubmission(sqlException)) {
                    showResult(request, response, false, "You have already attempted this mission. Each mission allows one attempt.", 0);
                    return;
                }
                throw exception;
            }
        } catch (SQLException | ArithmeticException exception) {
            getServletContext().log("Could not submit mission answer", exception);
            showResult(request, response, false, "Your answer could not be saved. Please try again.", 0);
            return;
        }

        if (correct) {
            showResult(request, response, true, "Correct! You earned " + reward + " XP.", reward);
        } else {
            showResult(request, response, false, "Not quite. Your attempt has been recorded, and this mission cannot be retried.", 0);
        }
    }

    private boolean isDuplicateSubmission(SQLException exception) {
        SQLException current = exception;
        while (current != null) {
            if ("23000".equals(current.getSQLState()) && current.getErrorCode() == 1062) {
                return true;
            }
            current = current.getNextException();
        }
        return false;
    }

    private void showResult(HttpServletRequest request, HttpServletResponse response,
                            boolean correct, String message, int xpEarned)
            throws ServletException, IOException {
        request.setAttribute("correct", correct);
        request.setAttribute("resultMessage", message);
        request.setAttribute("xpEarned", xpEarned);
        request.getRequestDispatcher("/mission-result.jsp").forward(request, response);
    }
}