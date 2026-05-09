package com.example.tarea.controller;

import com.example.tarea.dto.*;
import com.example.tarea.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    // POST /flights/create (sin protección)
    @PostMapping("/create")
    public ResponseEntity<FlightResponseDto> createFlight(@Valid @RequestBody RequestFlightDto dto) {
        FlightResponseDto response = flightService.createFlight(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /flights/{id} (protegido)
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    // GET /flights/search (protegido)
    // Parámetros opcionales: flightNumber, airlineName, startDate, endDate
    @GetMapping("/search")
    public ResponseEntity<List<FlightResponseDto>> searchFlights(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<FlightResponseDto> results = flightService.searchFlights(flightNumber, airlineName, startDate, endDate);
        return ResponseEntity.ok(results);
    }
}
