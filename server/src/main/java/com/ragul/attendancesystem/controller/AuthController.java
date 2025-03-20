package com.ragul.attendancesystem.controller;

import com.ragul.attendancesystem.exception.InvalidCredentialsException;
import com.ragul.attendancesystem.model.User;
import com.ragul.attendancesystem.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user) {
        logger.info("Login request received - User: {}", user);

        try {
            String token = authService.authenticate(user.getUsername(), user.getPassword());
            logger.info("Login successful - Token generated");
            return ResponseEntity.ok(Map.of("token", token));
        } catch (InvalidCredentialsException ex) {
            logger.error("Login failed - Invalid credentials: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Login failed - Unexpected error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred"));
        }
    }
}
