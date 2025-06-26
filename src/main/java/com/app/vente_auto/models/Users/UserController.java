package com.app.vente_auto.models.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.app.vente_auto.models.ApiResponse;


import com.app.vente_auto.repository.UsersRepository;
import com.app.vente_auto.security.CustomUserDetailsService;
import com.app.vente_auto.security.JwtService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    CustomUserDetailsService userDetailsService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
    
        ApiResponse response = userService.register(registerRequest);
        return ResponseEntity.ok(response);
}

@PostMapping("/login")
public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
        ApiResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        ApiResponse errorResponse = new ApiResponse();
        errorResponse.setErrorCode(400);
        errorResponse.setErrorMessage("Erreur lors de la connexion: " + e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse); // Bad repnse
    }
    }

    @GetMapping("/verification")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Accès autorisé à une route sécurisée User !");
    }

    @PostMapping("/refresh-token")
public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
    ApiResponse response = new ApiResponse();

    try {
        // 1. Vérifie que le refreshToken n'est pas vide
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || refreshToken.isBlank()) {
            response.setErrorCode(400);
            response.setErrorMessage("Le refresh token est manquant");
            return ResponseEntity.badRequest().body(response);
        }

        // 2. Extrait l'email (username) depuis le token
        String email = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 3. Vérifie la validité du refresh token
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            response.setErrorCode(403);
            response.setErrorMessage("Refresh token invalide ou expiré");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        // 4. Génère un nouveau access token
        String newAccessToken = jwtService.generateToken(email);

        response.setErrorCode(200);
        response.setErrorMessage("Nouveau token généré avec succès");
        response.setToken(newAccessToken);
        response.setRefreshToken(refreshToken);

        return ResponseEntity.ok(response);
    

    } catch (Exception e) {
        response.setErrorCode(500);
        response.setErrorMessage("Erreur serveur : " + e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }
    }



}
