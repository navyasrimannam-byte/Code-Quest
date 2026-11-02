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
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/missions")
public class MissionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long userId = session == null ? null : (Long) session.getAttribute("userId");
        String sql = "SELECT m.id, m.title, m.description, m.category, m.difficulty, "
                + "m.option_a, m.option_b, m.option_c, m.option_d, m.xp_reward, "
                + "s.id AS submission_id, s.is_correct AS submission_correct "
                + "FROM missions m LEFT JOIN mission_submissions s "
                + "ON s.mission_id = m.id AND s.user_id = ? "
                + "WHERE m.active = TRUE ORDER BY m.category, m.id";
        List<Map<String, Object>> missions = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection(getServletContext());
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (userId == null) {
                statement.setNull(1, Types.BIGINT);
            } else {
                statement.setLong(1, userId);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> mission = new LinkedHashMap<>();
                    mission.put("id", result.getLong("id"));
                    mission.put("title", result.getString("title"));
                    mission.put("description", result.getString("description"));
                    mission.put("category", result.getString("category"));
                    mission.put("difficulty", result.getString("difficulty"));
                    mission.put("optionA", result.getString("option_a"));
                    mission.put("optionB", result.getString("option_b"));
                    mission.put("optionC", result.getString("option_c"));
                    mission.put("optionD", result.getString("option_d"));
                    mission.put("xpReward", result.getInt("xp_reward"));
                    mission.put("attempted", result.getObject("submission_id") != null);
                    mission.put("correct", result.getBoolean("submission_correct"));
                    missions.add(mission);
                }
            }
            request.setAttribute("missions", missions);
        } catch (SQLException exception) {
            getServletContext().log("Could not load missions", exception);
            request.setAttribute("error", "Missions could not be loaded. Check the database connection.");
        }
        request.getRequestDispatcher("/missions.jsp").forward(request, response);
    }
}