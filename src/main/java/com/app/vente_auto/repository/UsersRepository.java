package com.app.vente_auto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.vente_auto.models.Users.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    
    
    // Recherche un utilisateur par son email
     Optional<Users> findByEmail(String email);

    // Recherche un utilisateur par son id
    Optional<Users> findById(Long id);

}
