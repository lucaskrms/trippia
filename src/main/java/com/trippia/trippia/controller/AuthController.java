package com.trippia.trippia.controller;

import com.trippia.trippia.dto.UserResponseDTO;
import com.trippia.trippia.model.User;
import com.trippia.trippia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        UserResponseDTO response = new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
        return ResponseEntity.ok(response);
    }
}