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

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS mission_count FROM missions");
             ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                request.setAttribute("missionCount", result.getInt("mission_count"));
            }
        } catch (SQLException exception) {
            getServletContext().log("Could not load admin dashboard", exception);
            request.setAttribute("error", "Dashboard data could not be loaded.");
        }
        request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
    }
}