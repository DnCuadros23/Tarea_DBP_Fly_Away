package com.example.tarea.controller;

import com.example.tarea.dto.*;
import com.example.tarea.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponseDto> bookFlight(
            @Valid @RequestBody BookingRequestDto dto,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String email = "johndoe@gmail.com";

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String[] parts = token.split("\\.");
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
                if (payload.contains("\"sub\":\"")) {
                    email = payload.split("\"sub\":\"")[1].split("\"")[0];
                }
            } catch (Exception e) {
            }
        }

        BookingResponseDto response = bookingService.bookFlight(dto.getFlightId(), email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flights/book/{id}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
}