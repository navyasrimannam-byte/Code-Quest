package com.codequest.servlet;

import com.codequest.util.DBConnection;
import com.codequest.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String identity = request.getParameter("identity");
        String passwordText = request.getParameter("password");
        identity = identity == null ? "" : identity.trim();
        passwordText = passwordText == null ? "" : passwordText;
        char[] password = passwordText.toCharArray();
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, username, password_hash FROM users WHERE username = ? OR email = ?")) {
            statement.setString(1, identity);
            statement.setString(2, identity.toLowerCase());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next() && PasswordUtil.verify(password, result.getString("password_hash"))) {
                    HttpSession oldSession = request.getSession(false);
                    if (oldSession != null) {
                        oldSession.invalidate();
                    }
                    HttpSession session = request.getSession(true);
                    session.setAttribute("userId", result.getLong("id"));
                    session.setAttribute("username", result.getString("username"));
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }
            }
            request.setAttribute("error", "The username/email or password was not recognized.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (SQLException | GeneralSecurityException exception) {
            getServletContext().log("Student login failed", exception);
            request.setAttribute("error", "Login is temporarily unavailable. Check the database connection.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}