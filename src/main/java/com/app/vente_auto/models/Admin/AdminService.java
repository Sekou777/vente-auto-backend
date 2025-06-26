package com.app.vente_auto.models.Admin;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.app.vente_auto.models.Users.Users;
import com.app.vente_auto.repository.UsersRepository;
import com.app.vente_auto.security.JwtService;

@Service
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminService { 

    @Autowired
    UsersRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
