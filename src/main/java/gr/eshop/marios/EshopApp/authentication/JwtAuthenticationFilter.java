package gr.eshop.marios.EshopApp.authentication;


import gr.eshop.marios.EshopApp.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    /**
     * Determines if a request should bypass JWT filtering.
     *
     * @param request the HttpServletRequest to evaluate
     * @return true if the request should bypass filtering, false otherwise
     * @throws ServletException if an exception occurs during filtering
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

    /**
     * Applies internal JWT filtering for incoming HTTP requests.
     *
     * @param request the HttpServletRequest to process
     * @param response the HttpServletResponse to populate
     * @param filterChain the FilterChain to pass the request and response to the next filter
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an input or output exception occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        LOGGER.info("Incoming request: " + request.getRequestURI());

        // Εξαίρεση Swagger Endpoints
        if (shouldNotFilter(request)) {
            LOGGER.info("Skipping JWT validation for Swagger endpoint: " + request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String jwt;
        String username;
        String userRole;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOGGER.info("No Authorization header found. Skipping JWT validation for: " + request.getRequestURI());

            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            username = jwtService.extractSubject(jwt);
            userRole = jwtService.getStringClaim(jwt, "role");

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } else {
                LOGGER.warn("Token is not valid" + request.getRequestURI());
            }

        } catch (ExpiredJwtException e) {
            LOGGER.warn("WARN: Expired token", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String jsonBody = "{\"code\": \"expired token\", \"message\"" + e.getMessage() + "\" }";
            response.getWriter().write(jsonBody);
            return;
        } catch (Exception e) {
            LOGGER.warn("WARN: Something went wrong while parsing JWT ", e);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            String jsonBody = "{\"code\": \"invalid token\", \"description\"" + e.getMessage() + "\" }";
            response.getWriter().write(jsonBody);
            return;
        }

        filterChain.doFilter(request, response);

    }
}


