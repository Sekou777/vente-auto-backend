package com.app.vente_auto.models.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.vente_auto.models.Users.Users;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



import java.util.List;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/admin")
public class AdminController { 

    @Autowired
    AdminService userService;

    

@GetMapping("/secured")
public ResponseEntity<String> securedEndpoint() {
    return ResponseEntity.ok("Accès autorisé à une route sécurisée Admin !");
}

    @GetMapping("/allUsers")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/userById")
    public Users getUserById(Long id) {
        return userService.getUserById(id);
    }

}
