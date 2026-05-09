package com.example.tarea.controller;

import com.example.tarea.dto.*;
import com.example.tarea.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /users/register (sin protección)
    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> register(@Valid @RequestBody RequestUserDto dto) {
        ResponseUserDto response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /users/{id} (protegido)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(new ResponseUserDto(user.getId()));
    }
}
