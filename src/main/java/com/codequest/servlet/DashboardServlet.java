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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        long userId = (Long) session.getAttribute("userId");
        String sql = "SELECT u.username, u.xp, u.level, "
                + "COUNT(CASE WHEN s.is_correct = TRUE THEN 1 END) AS completed "
                + "FROM users u LEFT JOIN mission_submissions s ON s.user_id = u.id "
                + "WHERE u.id = ? GROUP BY u.id, u.username, u.xp, u.level";
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int xp = result.getInt("xp");
                    request.setAttribute("username", result.getString("username"));
                    request.setAttribute("xp", xp);
                    request.setAttribute("level", result.getInt("level"));
                    request.setAttribute("completed", result.getInt("completed"));
                    request.setAttribute("levelProgress", xp % 100);
                    request.setAttribute("xpToNext", 100 - (xp % 100));
                } else {
                    session.invalidate();
                    response.sendRedirect(request.getContextPath() + "/login.jsp?auth=required");
                    return;
                }
            }
        } catch (SQLException exception) {
            getServletContext().log("Could not load student dashboard", exception);
            request.setAttribute("error", "Your progress could not be loaded. Please try again.");
        }
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}