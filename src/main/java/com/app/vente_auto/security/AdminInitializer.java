package com.app.vente_auto.security;

import com.app.vente_auto.models.Users.Role;
import com.app.vente_auto.models.Users.Users;
import com.app.vente_auto.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                Users admin = new Users();
                admin.setName("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123")); 
                admin.setRole(Role.ADMIN);
                admin.setPhone("");
                admin.setEmailVerified(true);

                userRepository.save(admin);
                System.out.println("Admin créé avec succès !");
            } else {
              //  System.out.println(" Admin existe déjà.");
            }
        };
    }
}