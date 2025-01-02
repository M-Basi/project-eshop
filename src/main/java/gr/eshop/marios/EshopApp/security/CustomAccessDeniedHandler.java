package gr.eshop.marios.EshopApp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handles AccessDeniedException by sending a custom JSON response with status 403 (Forbidden).
     *
     * @param request the HttpServletRequest that resulted in the exception
     * @param response the HttpServletResponse to be customized
     * @param accessDeniedException the exception that was thrown when access was denied
     * @throws IOException if an input or output exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Set the response status and content type
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        // Write a custom JSON response with the collected information
        String json = "{\"code\": \"userNotAuthorized\", \"description\": \"User is not allowed to access this endpoint.\"}";
        response.getWriter().write(json);
    }
}
