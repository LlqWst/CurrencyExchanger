package org.example.response_utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {
    private ResponseUtils() {
    }

    private static String toJson(Object object) throws IOException {
        if(object == null){
            return null;
        }
        String json;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        json = mapper.writeValueAsString(object);
        return json;
    }

    public static void sendJson(HttpServletResponse res, Object payload, int status) throws IOException{
        String json = toJson(payload);
        res.setStatus(status);
        if (json != null){
            PrintWriter pw = res.getWriter();
            pw.write(json);
            pw.close();
        }
    }

    public static void sendError(HttpServletResponse res, String message, int status) throws IOException {
        res.setStatus(status);
        res.getWriter().write("{\"message\":\"" + message + "\"}");
    }

}
