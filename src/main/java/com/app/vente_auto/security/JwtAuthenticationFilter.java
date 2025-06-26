package com.app.vente_auto.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// Ce filtre est exécuté une fois par requête HTTP
// Il vérifie si l'utilisateur est authentifié via un token JWT dans l'en-tête

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Récupère le header "Authorization"
        final String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // 2. Vérifie si l'en-tête commence par "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 3. Extrait le token (retire "Bearer ")
            token = authHeader.substring(7);

            // 4. Extrait l'email à partir du token
            email = jwtService.extractEmail(token);
        }

        // 5. Si on a bien un email et que personne n'est encore authentifié
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Charge l'utilisateur à partir de l'email
            var userDetails = userDetailsService.loadUserByUsername(email);

            // 7. Vérifie que le token est bien valide pour cet utilisateur
            if (jwtService.isTokenValid(token, userDetails)) {

                // 8. Crée un objet d'authentification
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 9. Ajoute les détails de la requête
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. Dis à Spring Security que cet utilisateur est authentifié
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Continue avec le prochain filtre (ou le contrôleur si tout est bon)
        filterChain.doFilter(request, response);
    }
}
