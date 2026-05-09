package com.example.tarea.controller;

import com.example.tarea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CleanupController {

    private final BookingRepository bookingRepository;
    private final FlightRepository  flightRepository;
    private final UserRepository    userRepository;

    // DELETE /cleanup (sin protección) - Limpia completamente la BD para tests
    @DeleteMapping("/cleanup")
    public ResponseEntity<Map<String, String>> cleanup() {
        bookingRepository.deleteAll();
        flightRepository.deleteAll();
        userRepository.deleteAll();
        return ResponseEntity.ok(Map.of("message", "Base de datos limpiada exitosamente"));
    }
}
