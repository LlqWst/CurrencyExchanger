package dev.lqwd.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import java.io.IOException;


@WebFilter({"/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange/*"})
public class ContentTypeFilter extends  HttpFilter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        servletResponse.setContentType("application/json");

        super.doFilter(servletRequest, servletResponse, chain);
    }
}
