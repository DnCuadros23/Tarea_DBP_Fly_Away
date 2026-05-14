package com.example.tarea.service;
import com.example.tarea.dto.*;
import com.example.tarea.exception.BadRequestException;
import com.example.tarea.exception.UnauthorizedException;
import com.example.tarea.repository.UserRepository;
import com.example.tarea.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDto login(LoginRequestDto dto) {
        var user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Email no registrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponseDto(token);
    }
}
