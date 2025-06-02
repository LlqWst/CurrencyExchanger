package dev.lqwd.controller;

import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;

public class BasicServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doResponse(HttpServletResponse response, int status, Object value) throws IOException {
        response.setStatus(status);
        objectMapper.writeValue(response.getWriter(), value);
    }

    protected String getPathParameter(HttpServletRequest req) {

        return req.getPathInfo().replaceFirst("/", "");
    }

}