package com.shahed.SpringSecEx.Configuration;

import com.shahed.SpringSecEx.service.JWTService;
import com.shahed.SpringSecEx.service.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    // Constructor injection
    public JWTFilter(JWTService jwtService, MyUserDetailsService myUserDetailsService) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the request is for a public endpoint (that doesn't require JWT validation)
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response); // Skip further processing
            return;
        }

        // If Authorization header exists and contains a Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract the token
            try {
                username = jwtService.extractUserName(token); // Extract the username from the token
            } catch (ExpiredJwtException | SignatureException e) {
                // Handle invalid or expired JWT
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT token");
                return; // Skip further filter chain processing
            }
        }

        // Proceed if the token and username are valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the service
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            // Validate token
            if (jwtService.validateToken(token, userDetails)) {
                // Create authentication token
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // Define public endpoints that don't need JWT validation
        String[] publicEndpoints = {"/register", "/login", "/refreshToken", "/swagger-ui/**", "/v3/api-docs/**"};

        for (String endpoint : publicEndpoints) {
            // Check if the URI matches exactly or starts with the public endpoint
            if (uri.equals(endpoint) || uri.startsWith(endpoint + "/")) {
                return true; // Public endpoint found
            }
        }
        return false; // Not a public endpoint
    }
}