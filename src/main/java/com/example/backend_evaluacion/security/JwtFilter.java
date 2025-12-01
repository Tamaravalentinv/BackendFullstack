package com.example.backend_evaluacion.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend_evaluacion.service.UsuarioService;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, java.io.IOException {

        String header = req.getHeader("Authorization");
        logger.debug("Processing request: {} {}", req.getMethod(), req.getRequestURI());
        logger.debug("Authorization header present: {}", header != null);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            try {
                Claims claims = jwtUtil.obtenerClaims(token);
                String username = claims.getSubject();
                String rolFromToken = claims.get("rol", String.class);
                
                logger.info("JWT validated for user: {}, role: {}", username, rolFromToken);

                // Create authorities from token
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (rolFromToken != null && !rolFromToken.isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + rolFromToken));
                }

                // Create authentication token
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
                );
                
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
                
                logger.info("Authentication set for user: {} with authorities: {}", username, authorities);

            } catch (Exception e) {
                logger.warn("JWT validation failed: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            logger.debug("No Bearer token found in request");
        }

        chain.doFilter(req, res);
    }
}
