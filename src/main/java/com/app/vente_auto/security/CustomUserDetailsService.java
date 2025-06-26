package com.app.vente_auto.security;


import com.app.vente_auto.models.Users.Users;
import com.app.vente_auto.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
// Ce service implémente UserDetailsService pour charger les détails de l'utilisateur
// à partir de la base de données.
// Il est utilisé par Spring Security pour authentifier les utilisateurs.
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

        @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
            
        // ⬇️ Rôle converti en autorité pour Spring Security
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}

/*
 * Résumé :
Ce service cherche l’utilisateur en BDD à partir de l’email.

Il construit un objet UserDetails que Spring Security comprend.
 */


