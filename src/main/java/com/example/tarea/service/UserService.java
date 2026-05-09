package com.example.tarea.service;

import com.example.tarea.model.User;
import com.example.tarea.dto.*;
import com.example.tarea.exception.BadRequestException;
import com.example.tarea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseUserDto register(RequestUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        User saved = userRepository.save(user);
        return new ResponseUserDto(saved.getId());
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new com.example.tarea.exception.ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));
    }
}
