<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Admin dashboard | CodeQuest</title><link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"></head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> ADMIN CONSOLE</p><h1>Manage the playing field.</h1><p>Signed in as <strong><%= Html.escape(session.getAttribute("adminUsername")) %></strong>. Create clear, focused challenges for students.</p></div>
    <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
    <div class="stats-grid"><div class="stat"><span class="stat-label">TOTAL MISSIONS</span><strong class="stat-value"><%= Html.escape(request.getAttribute("missionCount")) %></strong></div></div>
    <div class="content-panel"><div class="panel-heading"><h2>Question management</h2></div><div class="admin-actions"><a class="button button-primary" href="<%= request.getContextPath() %>/admin/add-question">Add a mission <span aria-hidden="true">↗</span></a><a class="button button-quiet" href="<%= request.getContextPath() %>/admin/questions">View all missions</a></div></div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>