package com.app.vente_auto.repository;

import com.app.vente_auto.models.EmailConfirmationToken;
import com.app.vente_auto.models.Users.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    Optional<EmailConfirmationToken> findByToken(String token);

    void deleteAllByUser(Users user);

}