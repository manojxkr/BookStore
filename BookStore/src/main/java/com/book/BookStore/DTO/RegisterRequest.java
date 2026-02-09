package com.book.BookStore.DTO;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    
}
