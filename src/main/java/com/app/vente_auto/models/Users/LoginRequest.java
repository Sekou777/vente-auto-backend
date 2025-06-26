package com.app.vente_auto.models.Users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {

    @NotBlank(message = "Email est obligatoire")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail.com$", message = "Email invalide. Format attendu : exemple@gmail.com")
    private String email;

    @NotBlank(message = "Mot de passe est obligatoire")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
