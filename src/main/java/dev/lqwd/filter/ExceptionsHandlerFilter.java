package dev.lqwd.filter;
import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.ErrorResponseDto;
import dev.lqwd.exception.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter("/*")
public class ExceptionsHandlerFilter extends HttpFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpRes = (HttpServletResponse) servletResponse;

        try {
            chain.doFilter(httpReq, httpRes);
        } catch (BadRequestException e) {
            writeError(httpRes, SC_BAD_REQUEST, e);
        } catch (NotFoundException e) {
            writeError(httpRes, SC_NOT_FOUND, e);
        } catch (ExistInDataBaseException e) {
            writeError(httpRes, SC_CONFLICT, e);
        } catch (DataBaseException e) {
            writeError(httpRes, SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    private void writeError(HttpServletResponse httpRes, int status, Exception e) throws IOException {
        httpRes.setStatus(status);
        ErrorResponseDto errorDto = new ErrorResponseDto(e.getMessage());
        objectMapper.writeValue(httpRes.getWriter(), errorDto);
    }
}
