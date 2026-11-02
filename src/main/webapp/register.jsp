<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create account | CodeQuest</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading">
        <p class="eyebrow"><span class="status-dot"></span> YOUR QUEST BEGINS</p>
        <h1>Create your account</h1>
        <p>Start at Level 1, then earn your way forward one mission at a time.</p>
    </div>
    <div class="form-panel">
        <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
        <form action="<%= request.getContextPath() %>/register" method="post">
            <div class="form-grid">
                <div class="field field-full"><label for="username">Username</label><input id="username" name="username" autocomplete="username" minlength="3" maxlength="40" pattern="[A-Za-z0-9_]{3,40}" required></div>
                <div class="field field-full"><label for="email">Email address</label><input id="email" name="email" type="email" autocomplete="email" maxlength="254" required></div>
                <div class="field"><label for="password">Password</label><input id="password" name="password" type="password" autocomplete="new-password" minlength="8" maxlength="128" required></div>
                <div class="field"><label for="confirmPassword">Confirm password</label><input id="confirmPassword" name="confirmPassword" type="password" autocomplete="new-password" minlength="8" maxlength="128" required></div>
            </div>
            <div class="form-actions"><button class="button button-primary" type="submit">Create account <span aria-hidden="true">↗</span></button></div>
        </form>
        <p class="form-note">Already registered? <a href="<%= request.getContextPath() %>/login.jsp">Log in</a></p>
    </div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>