package com.app.vente_auto.models.Users;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public class RegisterRequest {

    @NotBlank(message = "Nom est obligatoire")
    private String name;

    @NotBlank(message = "Email est obligatoire")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail.com$", message = "Email invalide. Format attendu : exemple@gmail.com")
    private String email;
    private String phone;

    @NotBlank(message = "Mot de passe est obligatoire")
    @jakarta.validation.constraints.Size(min = 6, message = "Mot de passe doit contenir au moins 6 caractères")
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
}
