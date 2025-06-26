package com.app.vente_auto.models;



import com.app.vente_auto.models.Users.Users;
import com.app.vente_auto.repository.EmailConfirmationTokenRepository;
import com.app.vente_auto.repository.UsersRepository;
import com.app.vente_auto.security.EmailConfirmationTokenService;
import com.app.vente_auto.security.EmailService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfirmationController {

    @Autowired
    EmailConfirmationTokenService tokenService;

    @Autowired
    private final UsersRepository userRepository;

    @Autowired
    EmailConfirmationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/confirm")
public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
    

    Optional<EmailConfirmationToken> optionalToken = tokenRepository.findByToken(token);

    // 🔍 Vérifie si le token existe
    if (optionalToken.isEmpty()) {
       return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("https://sekou777.github.io/vente-auto-pages/email-invalid.html"))
            .build();
    }

    EmailConfirmationToken confirmationToken = optionalToken.get();

    // Vérifie si le lien est expiré
    if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("https://sekou777.github.io/vente-auto-pages/email-expired.html"))
            .build();
    }

    Users user = confirmationToken.getUser();

    // ✅ Vérifie si l'e-mail est déjà confirmé
    if (user.isEmailVerified()) {
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("https://sekou777.github.io/vente-auto-pages/email-already-confirmed.html"))
            .build();
    }

    // ✔️ Confirmation de l'email
    user.setEmailVerified(true);
    userRepository.save(user);

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create("https://sekou777.github.io/vente-auto-pages/email-confirmed-success.html"))
        .build();
}

@PostMapping("/resend-confirmation")
public ResponseEntity<?> resendConfirmationEmail(@RequestParam("email") String email) {

    Optional<Users> userOptional = userRepository.findByEmail(email);
    if (userOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Aucun utilisateur trouvé avec cet email.");
    }

    Users user = userOptional.get();

    if (user.isEmailVerified()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Cet email est déjà confirmé.");
    }

    // Supprimer anciens tokens associés à cet utilisateur (optionnel)
    tokenRepository.deleteAllByUser(user);

    // Générer un nouveau token
    String newTokenStr = UUID.randomUUID().toString();
    EmailConfirmationToken newToken = new EmailConfirmationToken(
        newTokenStr,
        LocalDateTime.now(),
        LocalDateTime.now().plusMinutes(30),
        user
    );

    tokenService.saveToken(newToken);

    // Construire le lien de confirmation
    String confirmationLink = "http://localhost:8081/api/confirm?token=" + newTokenStr;
    String emailContent = tokenService.buildEmail(user.getName(), confirmationLink);

    // Envoyer l’email (ici tu peux appeler ton service mail)
    // Par exemple : emailService.send(user.getEmail(), emailContent);
    System.out.println("Email de confirmation renvoyé : \n" + emailContent);
    try {
        emailService.sendEmail(user.getEmail(), "Confirmation de votre email", emailContent);
    } catch (java.io.IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Erreur lors de l'envoi de l'email de confirmation.");
    }

    return ResponseEntity.ok("Un nouveau lien de confirmation a été envoyé à votre email.");
}

}
