package com.example.tarea.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightResponseDto {
    private Long id;
    private String flightNumber;
    private String airlineName;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
}
