<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Student login | CodeQuest</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> WELCOME BACK</p><h1>Pick up your quest.</h1><p>Sign in to see your progress and continue solving.</p></div>
    <div class="form-panel">
        <% if ("1".equals(request.getParameter("registered"))) { %><p class="message message-success">Account created. You can log in now.</p><% } %>
        <% if ("required".equals(request.getParameter("auth"))) { %><p class="message message-info">Sign in to access your dashboard and submit answers.</p><% } %>
        <% if ("1".equals(request.getParameter("logout"))) { %><p class="message message-success">You have been logged out.</p><% } %>
        <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
        <form action="<%= request.getContextPath() %>/login" method="post">
            <div class="field"><label for="identity">Username or email</label><input id="identity" name="identity" autocomplete="username" required></div>
            <div class="field" style="margin-top: 16px"><label for="password">Password</label><input id="password" name="password" type="password" autocomplete="current-password" required></div>
            <div class="form-actions"><button class="button button-primary" type="submit">Log in <span aria-hidden="true">↗</span></button></div>
        </form>
        <p class="form-note">New to CodeQuest? <a href="<%= request.getContextPath() %>/register.jsp">Create an account</a></p>
    </div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>