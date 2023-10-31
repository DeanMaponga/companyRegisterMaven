package com.example.companyRegister.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getRequestURI().contains("/api")  && !request.getRequestURI().contains("/api-docs")) {
                final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    if (request.getRequestURI().contains("/api/v1/auth")) {
                        filterChain.doFilter(request, response);
                    }
                    else {
                        response.sendError(403);
                    }
                    return;
                }
                final String jwt = authHeader.substring(7);
                final String userEmail = jwtService.extractUsername(jwt);
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                    filterChain.doFilter(request, response);
                } else if (userEmail != null) {
                    filterChain.doFilter(request, response);
                }
            }
            else{
                //non api routes
                System.out.println(request.getRequestURI());
                filterChain.doFilter(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(403);
        }
    }
}
