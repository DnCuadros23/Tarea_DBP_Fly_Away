package com.example.tarea.dto;

// ¡No olvides este import!
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {

    private Long id;
    private LocalDateTime bookingDate;
    private Long flightId;
    private String flightNumber;

    @JsonProperty("customerId")
    private Long userId;
    private String customerFirstName;
    private String customerLastName;
    @JsonProperty("estDepartureTime")
    private LocalDateTime departureTime;

    @JsonProperty("estArrivalTime")
    private LocalDateTime arrivalTime;
    private String airlineName;
    private String origin;
    private String destination;
}