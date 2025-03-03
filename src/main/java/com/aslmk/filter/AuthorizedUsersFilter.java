package com.aslmk.filter;

import com.aslmk.util.CookieUtil;
import groovy.util.logging.Slf4j;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Slf4j
public class AuthorizedUsersFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthorizedUsersFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);

        if (sessionIdFromCookie != null) {
            log.info("Authorized user attempted to access /login or /register page. Redirecting to /locations.");
            response.sendRedirect("/locations");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
