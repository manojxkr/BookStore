package com.book.BookStore.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.BookStore.DTO.LoginRequest;
import com.book.BookStore.DTO.RegisterRequest;
import com.book.BookStore.Response.Response;
import com.book.BookStore.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;

    }

    @PostMapping("/register")
    public ResponseEntity<Response<Void>> register(@RequestBody RegisterRequest req) {
        service.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response<>(true, "User registered successfully", null));

    }

    @PostMapping("/login")
    public ResponseEntity<Response<Map<String, String>>> login(@RequestBody LoginRequest req) {
        String token = service.login(req);
        // System.out.println(token);
        return ResponseEntity.ok(
                new Response<>(true, "Login success", Map.of("token", token)));
    }

}
