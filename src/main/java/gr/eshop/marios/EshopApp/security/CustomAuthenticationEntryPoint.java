package gr.eshop.marios.EshopApp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.eshop.marios.EshopApp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String requestedUri = request.getRequestURI();
        if (requestedUri.startsWith("/swagger-ui") || requestedUri.startsWith("/v3/api-docs")) {
            LOGGER.info("Skipping authentication error for Swagger endpoint: {}", requestedUri);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (requestedUri.startsWith("/swagger-ui") || requestedUri.startsWith("/v3/api-docs")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        }


        LOGGER.warn("Unauthorized access attempt to: {}", request.getRequestURI());
        // Αν υπάρχει αυθεντικοποιημένος χρήστης στον SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.getPrincipal() instanceof User)
                ? ((User) authentication.getPrincipal()).getUsername()
                : "unknown";

        String role = "unknown"; // Προεπιλογή αν δεν υπάρχει ρόλος
        if (authentication != null && authentication.getAuthorities() != null) {
            role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst().orElse("unknown");
        }


        // Set the response status to 401 unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String json = objectMapper.writeValueAsString(Map.of(
                "code", "userNotAuthenticated",
                "description", "User must authenticate in order to access this endpoint",
                "username", username,
                "role", role,
                "path", request.getRequestURI(),
                "timestamp", System.currentTimeMillis()
        ));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}