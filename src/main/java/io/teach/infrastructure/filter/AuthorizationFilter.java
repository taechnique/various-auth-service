package io.teach.infrastructure.filter;

import javax.servlet.*;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthorizationFilter.init()");
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        System.out.println("AuthorizationFilter.destroy()");
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("AuthorizationFilter.doFilter()");
    }
}
