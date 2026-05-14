package com.example.tarea.controller;

import com.example.tarea.dto.*;
import com.example.tarea.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // POST /flights/create (sin protección)
    @PostMapping("/create")
    public ResponseEntity<FlightResponseDto> createFlight(@Valid @RequestBody FlightRequestDto dto) {
        FlightResponseDto response = flightService.createFlight(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponseDto>> searchFlights(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(value = "estDepartureTimeFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "estDepartureTimeTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<FlightResponseDto> results = flightService.searchFlights(flightNumber, airlineName, startDate, endDate);
        return ResponseEntity.ok(results);
    }
    @PostMapping("/create-many")
    public ResponseEntity<List<FlightResponseDto>> createManyFlights(@RequestBody java.util.Map<String, List<FlightRequestDto>> payload) {
        List<FlightRequestDto> inputs = payload.get("inputs");

        List<FlightResponseDto> responses = inputs.stream()
                .map(flightService::createFlight)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}
