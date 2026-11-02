package com.codequest.servlet;

import com.codequest.util.DBConnection;
import com.codequest.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = value(request, "username").trim();
        String email = value(request, "email").trim().toLowerCase();
        String passwordText = value(request, "password");
        String confirm = value(request, "confirmPassword");
        String error = null;

        if (!username.matches("[A-Za-z0-9_]{3,40}")) {
            error = "Username must be 3 to 40 characters and use letters, numbers, or underscores.";
        } else if (email.length() > 254 || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            error = "Enter a valid email address.";
        } else if (passwordText.length() < 8 || passwordText.length() > 128) {
            error = "Password must be between 8 and 128 characters.";
        } else if (!passwordText.equals(confirm)) {
            error = "The passwords do not match.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        char[] password = passwordText.toCharArray();
        try (Connection connection = DBConnection.getConnection(getServletContext())) {
            try (PreparedStatement check = connection.prepareStatement(
                    "SELECT id FROM users WHERE username = ? OR email = ?")) {
                check.setString(1, username);
                check.setString(2, email);
                try (ResultSet result = check.executeQuery()) {
                    if (result.next()) {
                        request.setAttribute("error", "That username or email is already registered.");
                        request.getRequestDispatcher("/register.jsp").forward(request, response);
                        return;
                    }
                }
            }
            String passwordHash = PasswordUtil.hash(password);
            try (PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)")) {
                insert.setString(1, username);
                insert.setString(2, email);
                insert.setString(3, passwordHash);
                insert.executeUpdate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp?registered=1");
        } catch (SQLException | GeneralSecurityException exception) {
            getServletContext().log("Student registration failed", exception);
            request.setAttribute("error", "Registration could not be completed. Check the database setup and try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value;
    }
}