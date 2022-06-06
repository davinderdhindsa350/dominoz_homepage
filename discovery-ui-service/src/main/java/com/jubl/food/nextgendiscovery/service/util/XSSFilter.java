package com.jubl.food.nextgendiscovery.service.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = "/*")
public class XSSFilter implements Filter {

    /**
     *@param filterConfig
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    /**
     *
     */
    @Override
    public void destroy() {
    }

    /**
     *@param request
     *@param response
     *@param chain
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {
        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
    }
}
