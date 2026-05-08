package com.example.tarea.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private Long id;
    private Long userId;
    private String customerFirstName;
    private String customerLastName;
    private Long flightId;
    private String flightNumber;
    private String airlineName;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime bookingDate;
}
