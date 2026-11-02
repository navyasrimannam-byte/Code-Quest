package com.codequest.servlet;

import com.codequest.util.DBConnection;
import com.codequest.util.PasswordUtil;
import jakarta.servlet.ServletException;
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

public class AdminLoginServlet extends HttpServlet {
    private String pageTitle;

    @Override
    public void init() throws ServletException {
        pageTitle = getServletConfig().getInitParameter("pageTitle");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.getRequestDispatcher("/admin-login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String passwordText = request.getParameter("password");
        username = username == null ? "" : username.trim();
        char[] password = passwordText == null ? new char[0] : passwordText.toCharArray();
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, username, password_hash FROM admins WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next() && PasswordUtil.verify(password, result.getString("password_hash"))) {
                    HttpSession oldSession = request.getSession(false);
                    if (oldSession != null) {
                        oldSession.invalidate();
                    }
                    HttpSession session = request.getSession(true);
                    session.setAttribute("adminId", result.getLong("id"));
                    session.setAttribute("adminUsername", result.getString("username"));
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    return;
                }
            }
            request.setAttribute("error", "The administrator username or password was not recognized.");
            doGet(request, response);
        } catch (SQLException | GeneralSecurityException exception) {
            getServletContext().log("Administrator login failed", exception);
            request.setAttribute("error", "Admin login is temporarily unavailable. Check the database connection.");
            doGet(request, response);
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}