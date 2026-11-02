package com.codequest.servlet;

import com.codequest.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/admin/add-question")
public class AddQuestionServlet extends HttpServlet {
    private static final Set<String> CATEGORIES = Set.of("Java", "SQL", "DSA");
    private static final Set<String> DIFFICULTIES = Set.of("Easy", "Medium", "Hard");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/add-question.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String title = value(request, "title").trim();
        String description = value(request, "description").trim();
        String category = value(request, "category");
        String difficulty = value(request, "difficulty");
        String optionA = value(request, "optionA").trim();
        String optionB = value(request, "optionB").trim();
        String optionC = value(request, "optionC").trim();
        String optionD = value(request, "optionD").trim();
        String correctAnswer = value(request, "correctAnswer");
        String rewardText = value(request, "xpReward");
        String error = null;
        int reward = 0;
        try {
            reward = Integer.parseInt(rewardText);
        } catch (NumberFormatException exception) {
            error = "XP reward must be a whole number between 1 and 1000.";
        }
        if (error == null && (title.isBlank() || title.length() > 120 || description.isBlank()
                || description.length() > 4000 || optionA.isBlank() || optionB.isBlank()
                || optionC.isBlank() || optionD.isBlank())) {
            error = "Complete every field. Titles are limited to 120 characters and descriptions to 4000.";
        } else if (error == null && (!CATEGORIES.contains(category) || !DIFFICULTIES.contains(difficulty)
                || !Set.of("A", "B", "C", "D").contains(correctAnswer))) {
            error = "Select a valid category, difficulty, and correct answer.";
        } else if (error == null && (optionA.length() > 500 || optionB.length() > 500
                || optionC.length() > 500 || optionD.length() > 500)) {
            error = "Each answer choice must be 500 characters or fewer.";
        } else if (error == null && (reward < 1 || reward > 1000)) {
            error = "XP reward must be between 1 and 1000.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/add-question.jsp").forward(request, response);
            return;
        }
        String sql = "INSERT INTO missions (title, description, category, difficulty, option_a, option_b, "
                + "option_c, option_d, correct_answer, xp_reward) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, category);
            statement.setString(4, difficulty);
            statement.setString(5, optionA);
            statement.setString(6, optionB);
            statement.setString(7, optionC);
            statement.setString(8, optionD);
            statement.setString(9, correctAnswer);
            statement.setInt(10, reward);
            statement.executeUpdate();
            response.sendRedirect(request.getContextPath() + "/admin/questions?created=1");
        } catch (SQLException exception) {
            getServletContext().log("Could not add mission", exception);
            request.setAttribute("error", "The question could not be saved. Check the database connection.");
            request.getRequestDispatcher("/WEB-INF/views/add-question.jsp").forward(request, response);
        }
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value;
    }
}