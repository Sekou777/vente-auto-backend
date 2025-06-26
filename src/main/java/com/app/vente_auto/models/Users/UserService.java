package com.app.vente_auto.models.Users;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.app.vente_auto.models.ApiResponse;
import com.app.vente_auto.models.EmailConfirmationToken;
import com.app.vente_auto.repository.UsersRepository;
import com.app.vente_auto.security.EmailConfirmationTokenService;
import com.app.vente_auto.security.EmailService;
import com.app.vente_auto.security.JwtService;

@Service
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserService {

    @Autowired
    UsersRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    EmailConfirmationTokenService tokenService;

    @Autowired
    EmailService emailService;



    //Register a new user
    public ApiResponse register(RegisterRequest request) {
        ApiResponse response = new ApiResponse();  
        Users user = new Users();

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setErrorCode(400);
            response.setErrorMessage("L'email saisi est déjà utilisé");
            return response;
        }

        

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRole(Role.USER); // valeur par défaut

        String email = user.getEmail();
        String token = jwtService.generateToken(email);

        String refreshToken = jwtService.generateRefreshToken(email);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // Générer un token unique de confirmation
        String confirmationToken = UUID.randomUUID().toString();

        //Créer l'objet EmailConfirmationToken
        EmailConfirmationToken Token = new EmailConfirmationToken(
            confirmationToken,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            user
        );
        // 💾 Sauvegarder le token dans la base
        tokenService.saveToken(Token);

        String confirmationLink = "http://localhost:8081/api/confirm?token=" + confirmationToken;
        String emailContent = tokenService.buildEmail(user.getName(), confirmationLink);

        // 👇 Tu enverras ça via un service d'envoi d'e-mail
        System.out.println("📧 Email à envoyer : \n" + emailContent);

        try {
            emailService.sendEmail(user.getEmail(), "Confirmation de votre email", emailContent);
        } catch (java.io.IOException e) {
            response.setErrorCode(500);
            response.setErrorMessage("Erreur lors de l'envoi de l'email de confirmation : " + e.getMessage());
            return response;
        }
        

        response.setErrorCode(200);
        response.setErrorMessage("Inscription réussie");
        response.setData(user);
        response.setRefreshToken(refreshToken);
        return response;

    }

    public ApiResponse login(LoginRequest request) {
        ApiResponse response = new ApiResponse();
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.isEmailVerified()) {
        response.setErrorCode(403);
        response.setErrorMessage("Votre adresse email n'est pas encore confirmée.");
        return response;
    }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.setErrorCode(401);
            response.setErrorMessage("Mot de passe invalide");
            return response;
        }

        String email = user.getEmail();
        String token = jwtService.generateToken(email);

        String refreshToken = jwtService.generateRefreshToken(email);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
       
        response.setErrorCode(200);
        response.setErrorMessage("Connexion réussie");
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }

}
