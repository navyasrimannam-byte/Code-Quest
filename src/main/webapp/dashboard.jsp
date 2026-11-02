<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Your dashboard | CodeQuest</title><link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"></head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> PLAYER PROFILE</p><h1>Welcome, <%= Html.escape(request.getAttribute("username")) %>.</h1><p>Your progress, one solved problem at a time.</p></div>
    <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } else { %>
    <div class="stats-grid">
        <div class="stat"><span class="stat-label">CURRENT LEVEL</span><strong class="stat-value"><%= Html.escape(request.getAttribute("level")) %></strong></div>
        <div class="stat"><span class="stat-label">TOTAL XP</span><strong class="stat-value"><%= Html.escape(request.getAttribute("xp")) %></strong></div>
        <div class="stat"><span class="stat-label">MISSIONS CLEARED</span><strong class="stat-value"><%= Html.escape(request.getAttribute("completed")) %></strong></div>
        <div class="stat"><span class="stat-label">NEXT LEVEL</span><strong class="stat-value"><%= Html.escape(request.getAttribute("xpToNext")) %><span style="font-size:.82rem"> XP</span></strong></div>
    </div>
    <div class="content-panel">
        <div class="panel-heading"><h2>Level <%= Html.escape(request.getAttribute("level")) %> progress</h2><span class="mono table-muted"><%= Html.escape(request.getAttribute("levelProgress")) %> / 100 XP</span></div>
        <div class="progress-wrap"><div class="progress-track" role="progressbar" aria-label="Progress to next level" aria-valuemin="0" aria-valuemax="100" aria-valuenow="<%= Html.escape(request.getAttribute("levelProgress")) %>"><div class="progress-fill" style="width: <%= Html.escape(request.getAttribute("levelProgress")) %>%"></div></div><div class="progress-caption"><span>Level <%= Html.escape(request.getAttribute("level")) %></span><span><%= Html.escape(request.getAttribute("xpToNext")) %> XP until level <%= ((Integer) request.getAttribute("level")) + 1 %></span></div></div>
        <div class="form-actions"><a class="button button-primary" href="<%= request.getContextPath() %>/missions">Find a mission <span aria-hidden="true">↗</span></a><a class="text-link" href="<%= request.getContextPath() %>/leaderboard">View rankings →</a></div>
    </div>
    <% } %>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>