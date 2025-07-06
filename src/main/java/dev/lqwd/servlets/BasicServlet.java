package dev.lqwd.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BasicServlet extends HttpServlet {

    protected void doResponse(HttpServletResponse resp, int status, Object value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setStatus(status);
        mapper.writeValue(resp.getWriter(), value);
    }

}
