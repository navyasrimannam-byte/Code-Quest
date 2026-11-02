<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Add mission | CodeQuest</title><link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"></head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> MISSION EDITOR</p><h1>Write a new challenge.</h1><p>Questions and answer keys are stored in the database; the correct answer is never shown on the student mission list.</p></div>
    <div class="form-panel">
        <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
        <form action="<%= request.getContextPath() %>/admin/add-question" method="post">
            <div class="form-grid">
                <div class="field field-full"><label for="title">Question title</label><input id="title" name="title" maxlength="120" required></div>
                <div class="field field-full"><label for="description">Question description</label><textarea id="description" name="description" maxlength="4000" required></textarea></div>
                <div class="field"><label for="category">Category</label><select id="category" name="category" required><option value="">Choose category</option><option>Java</option><option>SQL</option><option>DSA</option></select></div>
                <div class="field"><label for="difficulty">Difficulty</label><select id="difficulty" name="difficulty" required><option value="">Choose difficulty</option><option>Easy</option><option>Medium</option><option>Hard</option></select></div>
                <div class="field"><label for="optionA">Choice A</label><input id="optionA" name="optionA" maxlength="500" required></div>
                <div class="field"><label for="optionB">Choice B</label><input id="optionB" name="optionB" maxlength="500" required></div>
                <div class="field"><label for="optionC">Choice C</label><input id="optionC" name="optionC" maxlength="500" required></div>
                <div class="field"><label for="optionD">Choice D</label><input id="optionD" name="optionD" maxlength="500" required></div>
                <div class="field"><label for="correctAnswer">Correct answer</label><select id="correctAnswer" name="correctAnswer" required><option value="">Select correct choice</option><option value="A">Choice A</option><option value="B">Choice B</option><option value="C">Choice C</option><option value="D">Choice D</option></select></div>
                <div class="field"><label for="xpReward">XP reward</label><input id="xpReward" name="xpReward" type="number" min="1" max="1000" required></div>
            </div>
            <div class="form-actions"><button class="button button-primary" type="submit">Publish mission <span aria-hidden="true">↗</span></button><a class="text-link" href="<%= request.getContextPath() %>/admin/dashboard">Cancel</a></div>
        </form>
    </div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>