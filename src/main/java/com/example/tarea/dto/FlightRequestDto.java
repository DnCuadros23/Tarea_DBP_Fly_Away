package com.example.tarea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightRequestDto {

    @NotBlank(message = "El número de vuelo es requerido")
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$", message = "Formato de número de vuelo inválido")
    private String flightNumber;

    @NotBlank(message = "La aerolínea es requerida")
    private String airlineName;

    @JsonProperty("estDepartureTime")
    @NotNull(message = "La hora de salida es requerida")
    private LocalDateTime estDepartureTime;

    @JsonProperty("estArrivalTime")
    @NotNull(message = "La hora de llegada es requerida")
    private LocalDateTime estArrivalTime;

    @NotNull(message = "Los asientos son requeridos")
    @Min(value = 1, message = "Debe haber al menos 1 asiento disponible")
    private Integer availableSeats;

    private String origin;
    private String destination;
}