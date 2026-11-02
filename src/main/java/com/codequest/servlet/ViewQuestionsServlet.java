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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/questions")
public class ViewQuestionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Map<String, Object>> questions = new ArrayList<>();
        String sql = "SELECT id, title, category, difficulty, xp_reward, active FROM missions ORDER BY id DESC";
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Map<String, Object> question = new LinkedHashMap<>();
                question.put("id", result.getLong("id"));
                question.put("title", result.getString("title"));
                question.put("category", result.getString("category"));
                question.put("difficulty", result.getString("difficulty"));
                question.put("xpReward", result.getInt("xp_reward"));
                question.put("active", result.getBoolean("active"));
                questions.add(question);
            }
            request.setAttribute("questions", questions);
        } catch (SQLException exception) {
            getServletContext().log("Could not load admin questions", exception);
            request.setAttribute("error", "Questions could not be loaded. Please try again.");
        }
        request.getRequestDispatcher("/WEB-INF/views/view-questions.jsp").forward(request, response);
    }
}