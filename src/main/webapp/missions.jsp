<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="java.util.List,java.util.Map,com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Missions | CodeQuest</title><link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"></head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> ACTIVE MISSIONS</p><h1>Choose your next challenge.</h1><p>Pick a track, think it through, and earn XP for every correct solution.</p></div>
    <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
    <% List<Map<String, Object>> missions = (List<Map<String, Object>>) request.getAttribute("missions"); %>
    <% if (missions == null || missions.isEmpty()) { %><div class="content-panel empty-state"><strong>No missions yet.</strong>Check back when the next challenge is published.</div><% } else { %>
    <div class="mission-list">
        <% for (Map<String, Object> mission : missions) { String difficulty = String.valueOf(mission.get("difficulty")); boolean attempted = Boolean.TRUE.equals(mission.get("attempted")); %>
        <article class="mission-card">
            <div class="mission-top"><div><h2><%= Html.escape(mission.get("title")) %></h2><p><%= Html.escape(mission.get("description")) %></p></div><div class="mission-tags"><span class="pill"><%= Html.escape(mission.get("category")) %></span><span class="pill pill-<%= difficulty.toLowerCase() %>"><%= Html.escape(difficulty) %></span><span class="pill pill-easy">+<%= Html.escape(mission.get("xpReward")) %> XP</span></div></div>
            <div class="mission-body">
                <% if (attempted) { %><p class="message <%= Boolean.TRUE.equals(mission.get("correct")) ? "message-success" : "" %>">Attempt recorded. <%= Boolean.TRUE.equals(mission.get("correct")) ? "Mission cleared." : "This mission allows one attempt." %></p>
                <% } else if (session.getAttribute("userId") == null) { %><p class="form-note">Log in to submit an answer. <a href="<%= request.getContextPath() %>/login.jsp">Student login</a></p>
                <% } else { %>
                <form action="<%= request.getContextPath() %>/submit-answer" method="post">
                    <input type="hidden" name="missionId" value="<%= Html.escape(mission.get("id")) %>">
                    <ul class="choice-list">
                        <li><label class="choice"><input type="radio" name="answer" value="A" required><span><strong>A.</strong> <%= Html.escape(mission.get("optionA")) %></span></label></li>
                        <li><label class="choice"><input type="radio" name="answer" value="B" required><span><strong>B.</strong> <%= Html.escape(mission.get("optionB")) %></span></label></li>
                        <li><label class="choice"><input type="radio" name="answer" value="C" required><span><strong>C.</strong> <%= Html.escape(mission.get("optionC")) %></span></label></li>
                        <li><label class="choice"><input type="radio" name="answer" value="D" required><span><strong>D.</strong> <%= Html.escape(mission.get("optionD")) %></span></label></li>
                    </ul>
                    <div class="mission-submit"><button class="button button-primary" type="submit">Submit answer <span aria-hidden="true">↗</span></button><span class="mono table-muted">ONE ATTEMPT</span></div>
                </form>
                <% } %>
            </div>
        </article>
        <% } %>
    </div><% } %>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>