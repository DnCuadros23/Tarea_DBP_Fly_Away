package com.example.tarea.controller;

import com.example.tarea.dto.*;
import com.example.tarea.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST /flights/book (protegido)
    @PostMapping("/book")
    public ResponseEntity<BookingResponseDto> bookFlight(
            @Valid @RequestBody BookingRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        BookingResponseDto response = bookingService.bookFlight(dto.getFlightId(), userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /flight/book/{id} (protegido)
    @GetMapping("/book/{id}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
}
