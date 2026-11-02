<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%@ page import="java.util.List,java.util.Map,com.codequest.util.Html" %>
<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Manage missions | CodeQuest</title><link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"></head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jspf" %>
<section class="page-shell">
    <div class="page-heading"><p class="eyebrow"><span class="status-dot"></span> ADMIN CONSOLE</p><h1>Mission library.</h1><p>Questions published to the CodeQuest mission board.</p></div>
    <% if ("1".equals(request.getParameter("created"))) { %><p class="message message-success">Mission published successfully.</p><% } %>
    <% if (request.getAttribute("error") != null) { %><p class="message"><%= Html.escape(request.getAttribute("error")) %></p><% } %>
    <% List<Map<String, Object>> questions = (List<Map<String, Object>>) request.getAttribute("questions"); %>
    <div class="content-panel"><div class="panel-heading"><h2>All questions</h2><a class="button button-primary button-small" href="<%= request.getContextPath() %>/admin/add-question">Add question +</a></div>
        <% if (questions == null || questions.isEmpty()) { %><div class="empty-state"><strong>No questions have been added.</strong>Create the first mission to populate the student board.</div><% } else { %>
        <div class="table-wrap"><table><thead><tr><th>TITLE</th><th>CATEGORY</th><th>DIFFICULTY</th><th>XP</th><th>STATUS</th></tr></thead><tbody>
            <% for (Map<String, Object> question : questions) { String difficulty = String.valueOf(question.get("difficulty")); %>
            <tr><td><strong><%= Html.escape(question.get("title")) %></strong></td><td class="table-muted"><%= Html.escape(question.get("category")) %></td><td><span class="pill pill-<%= difficulty.toLowerCase() %>"><%= Html.escape(difficulty) %></span></td><td><%= Html.escape(question.get("xpReward")) %> XP</td><td class="table-muted"><%= Boolean.TRUE.equals(question.get("active")) ? "Active" : "Archived" %></td></tr>
            <% } %>
        </tbody></table></div><% } %>
    </div>
</section>
<%@ include file="/WEB-INF/views/includes/footer.jspf" %>