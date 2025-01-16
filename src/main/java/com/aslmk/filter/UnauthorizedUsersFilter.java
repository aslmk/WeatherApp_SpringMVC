package com.aslmk.filter;

import com.aslmk.util.CookieUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UnauthorizedUsersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);

        if (sessionIdFromCookie == null) {
            // simple tmp log.
            System.out.println("Unauthorized access attempt!");
            response.sendRedirect("/auth/login");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
