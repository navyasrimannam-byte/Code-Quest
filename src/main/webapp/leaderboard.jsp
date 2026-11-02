<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="java.util.List,java.util.Map,com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><meta http-equiv="refresh" content="60"><title>Leaderboard | CodeQuest</title><link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"></head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> GLOBAL RANKINGS</p><h1>Who’s climbing?</h1><p>Ranked by total XP. Rankings refresh every 60 seconds.</p></div>
    <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
    <% List<Map<String, Object>> players = (List<Map<String, Object>>) request.getAttribute("players"); %>
    <div class="content-panel">
        <% if (players == null || players.isEmpty()) { %><div class="empty-state"><strong>No players yet.</strong>Be the first to register and earn XP.</div><% } else { %>
        <div class="table-wrap"><table><thead><tr><th>RANK</th><th>PLAYER</th><th>LEVEL</th><th>MISSIONS</th><th>TOTAL XP</th></tr></thead><tbody>
            <% for (Map<String, Object> player : players) { int rank = (Integer) player.get("rank"); %>
            <tr><td class="rank"><%= rank <= 3 ? "#" + rank : rank %></td><td><strong><%= Html.escape(player.get("username")) %></strong></td><td class="table-muted"><%= Html.escape(player.get("level")) %></td><td class="table-muted"><%= Html.escape(player.get("completed")) %></td><td><strong><%= Html.escape(player.get("xp")) %> XP</strong></td></tr>
            <% } %>
        </tbody></table></div><% } %>
    </div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>