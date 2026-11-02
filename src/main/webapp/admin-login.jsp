<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= Html.escape(request.getAttribute("pageTitle") == null ? "Admin login | CodeQuest" : request.getAttribute("pageTitle")) %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> ADMIN CONSOLE</p><h1>Administrator access</h1><p>Sign in to create and manage programming missions.</p></div>
    <div class="form-panel">
        <% if ("required".equals(request.getParameter("auth"))) { %><p class="message message-info">Administrator sign-in is required for that page.</p><% } %>
        <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
        <form action="<%= request.getContextPath() %>/admin/login" method="post">
            <div class="field"><label for="username">Admin username</label><input id="username" name="username" autocomplete="username" required></div>
            <div class="field" style="margin-top: 16px"><label for="password">Password</label><input id="password" name="password" type="password" autocomplete="current-password" required></div>
            <div class="form-actions"><button class="button button-primary" type="submit">Open console <span aria-hidden="true">↗</span></button></div>
        </form>
    </div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>