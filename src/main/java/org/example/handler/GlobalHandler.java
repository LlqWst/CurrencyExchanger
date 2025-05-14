package org.example.handler;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;


@WebFilter("/*")
public class GlobalHandler implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp= (HttpServletResponse) servletResponse;

        if (!isValid(httpReq)) {
            sendJsonError(httpResp, SC_BAD_REQUEST, "The requested resource is not available");
            return;
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (BadRequestException | CurrencyExistException | NotFoundException e){
            sendJsonError(httpResp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            sendJsonError(httpResp, SC_INTERNAL_SERVER_ERROR, "Data Base is busy");
        }
    }

    private void sendJsonError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write("{\"message\":\"" + message + "\"}");
    }

    private boolean isValid(HttpServletRequest req){
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String[] pathParts = path.split("/");
        String firstSegment = pathParts.length > 1 ? pathParts[1] : "";
        return path.equals("/") ||
                firstSegment.equals("currencies") ||
                firstSegment.equals("exchangeRates");
    }

}
