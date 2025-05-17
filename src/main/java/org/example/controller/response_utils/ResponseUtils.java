package org.example.controller.response_utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {
    private ResponseUtils() {
    }

    public static String toJson(Object object) throws IOException {
        if(object == null){
            return null;
        }
        String json;
        ObjectMapper mapper = new ObjectMapper();
        json = mapper.writeValueAsString(object);
        return json;
    }

    public static void sendResponse(HttpServletResponse res, String payload, int status) throws IOException{
        res.setStatus(status);
        if (payload != null){
            PrintWriter pw = res.getWriter();
            pw.write(payload);
            pw.close();
        }
    }

    public static void sendError(HttpServletResponse res, String message, int status) throws IOException {
        res.setStatus(status);
        res.getWriter().write("{\"message\":\"" + message + "\"}");
    }

}
