package com.app.vente_auto.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
// represente le point d'entrée pour les requêtes non authentifiées
// Si un utilisateur non authentifié tente d'accéder à une ressource protégée,
// cette classe gère la réponse en renvoyant un message d'erreur JSON
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", 403);
        errorDetails.put("errorMessage", "Accès refusé : authentification requise ou token invalide");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().println(mapper.writeValueAsString(errorDetails));
    }
}
