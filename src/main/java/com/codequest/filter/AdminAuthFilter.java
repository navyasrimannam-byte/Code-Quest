package com.codequest.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/admin/*")
public class AdminAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getServletPath();
        if ("/admin/login".equals(path) || "/admin/logout".equals(path)) {
            chain.doFilter(request, response);
            return;
        }
        if (httpRequest.getSession(false) == null
                || httpRequest.getSession(false).getAttribute("adminId") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin-login.jsp?auth=required");
            return;
        }
        chain.doFilter(request, response);
    }
}