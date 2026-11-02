<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Mission result | CodeQuest</title><link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"></head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <% boolean correct = Boolean.TRUE.equals(request.getAttribute("correct")); %>
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> MISSION RESULT</p><h1><%= correct ? "Challenge cleared." : "Attempt complete." %></h1></div>
    <div class="result-panel">
        <h2><%= correct ? "Nice work." : "Keep building your instincts." %></h2>
        <p><%= Html.escape(request.getAttribute("resultMessage")) %></p>
        <% if (correct) { %><p class="result-xp">+<%= Html.escape(request.getAttribute("xpEarned")) %> XP ADDED</p><% } %>
        <div class="form-actions"><a class="button button-primary" href="<%= request.getContextPath() %>/missions">Back to missions <span aria-hidden="true">↗</span></a><a class="text-link" href="<%= request.getContextPath() %>/dashboard">View progress →</a></div>
    </div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>