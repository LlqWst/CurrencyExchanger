package org.example.webfilter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.response_utils.ResponseUtils;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.example.exceptions.ErrorMessages.INCORRECT_URL;


@WebFilter("/*")
public class IncorrectUrlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes= (HttpServletResponse) res;

        if (!isValid(httpReq)) {
            ResponseUtils.sendError(httpRes, INCORRECT_URL.getMessage(), SC_NOT_FOUND);
            return;
        }
        chain.doFilter(req, res);

    }

    private boolean isValid(HttpServletRequest req){
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String[] pathParts = path.split("/");
        String firstSegment = pathParts.length > 1 ? pathParts[1] : "";
        return firstSegment.isEmpty() ||
                firstSegment.equals("currencies") ||
                firstSegment.equals("exchangeRates") ||
                firstSegment.equals("exchangeRate") ||
                firstSegment.equals("exchange");
    }

}
