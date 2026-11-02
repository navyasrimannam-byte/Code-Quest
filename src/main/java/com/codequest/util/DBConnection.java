package com.codequest.util;

import jakarta.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private DBConnection() {
    }

    public static Connection getConnection(ServletContext context) throws SQLException {
        String url = context.getInitParameter("dbUrl");
        String user = context.getInitParameter("dbUser");
        String password = context.getInitParameter("dbPassword");
        if (url == null || url.isBlank()) {
            throw new SQLException("The dbUrl context parameter is not configured.");
        }
        return DriverManager.getConnection(url, user == null ? "" : user, password == null ? "" : password);
    }
}