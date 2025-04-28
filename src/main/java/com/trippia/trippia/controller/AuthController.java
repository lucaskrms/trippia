package com.trippia.trippia.controller;

import com.trippia.trippia.dto.LoginRequestDTO;
import com.trippia.trippia.dto.LoginResponseDTO;
import com.trippia.trippia.dto.RegisterResponseDTO;
import com.trippia.trippia.model.User;
import com.trippia.trippia.service.UserService;
import com.trippia.trippia.config.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        RegisterResponseDTO response = new RegisterResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid e-mail or password");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @GetMapping("hello")
    public ResponseEntity<String > hello() {
        return ResponseEntity.ok("hello");
    }
}