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

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sql = "SELECT u.username, u.level, u.xp, "
                + "COUNT(CASE WHEN s.is_correct = TRUE THEN 1 END) AS completed "
                + "FROM users u LEFT JOIN mission_submissions s ON s.user_id = u.id "
                + "GROUP BY u.id, u.username, u.level, u.xp ORDER BY u.xp DESC, u.username ASC";
        List<Map<String, Object>> players = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            int rank = 1;
            while (result.next()) {
                Map<String, Object> player = new LinkedHashMap<>();
                player.put("rank", rank++);
                player.put("username", result.getString("username"));
                player.put("level", result.getInt("level"));
                player.put("xp", result.getInt("xp"));
                player.put("completed", result.getInt("completed"));
                players.add(player);
            }
            request.setAttribute("players", players);
        } catch (SQLException exception) {
            getServletContext().log("Could not load leaderboard", exception);
            request.setAttribute("error", "The leaderboard could not be loaded. Please try again.");
        }
        request.getRequestDispatcher("/leaderboard.jsp").forward(request, response);
    }
}