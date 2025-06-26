package com.app.vente_auto.security;

import com.app.vente_auto.models.EmailConfirmationToken;
import com.app.vente_auto.repository.EmailConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailConfirmationTokenService {

    @Autowired
    private EmailConfirmationTokenRepository tokenRepository;

    // Sauvegarder un nouveau token
    public void saveToken(EmailConfirmationToken token) {
        tokenRepository.save(token);
    }

    // Chercher un token par sa valeur
    public Optional<EmailConfirmationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    // Marquer comme confirmé
    public int setConfirmedAt(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> {
                    t.setConfirmedAt(LocalDateTime.now());
                    tokenRepository.save(t);
                    return 1;
                }).orElse(0);
    }

    public String buildEmail(String name, String link) {
    return "<html>" +
            "<body>" +
            "<h2>Bonjour " + name + ",</h2>" +
            "<p>Merci de vous être inscrit sur notre application 🚗 Vente Auto.</p>" +
            "<p>Veuillez confirmer votre adresse e-mail en cliquant sur le lien ci-dessous :</p>" +
            "<p><a href=\"" + link + "\">Confirmer mon adresse e-mail</a></p>" +
            "<p>Ce lien expirera dans <strong>15 minutes</strong>.</p>" +
            "<br><p>À bientôt !</p>" +
            "<p>L'équipe Vente Auto.</p>" +
            "</body>" +
            "</html>";
}

}

/*
 * Ce que ce service fait :

saveToken(...) → pour créer et enregistrer le token

getToken(...) → pour vérifier le token reçu depuis l’email

setConfirmedAt(...) → pour marquer un token comme utilisé
 */
