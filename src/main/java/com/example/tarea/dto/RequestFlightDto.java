package com.example.tarea.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestFlightDto {

    @NotBlank(message = "El número de vuelo es requerido")
    @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "El número de vuelo debe tener solo letras mayúsculas y números, máximo 6 caracteres")
    private String flightNumber;

    @NotBlank(message = "El nombre de la aerolínea es requerido")
    private String airlineName;

    @NotBlank(message = "El origen es requerido")
    private String origin;

    @NotBlank(message = "El destino es requerido")
    private String destination;

    @NotNull(message = "La hora de salida es requerida")
    private LocalDateTime departureTime;

    @NotNull(message = "La hora de llegada es requerida")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Los asientos disponibles son requeridos")
    @Min(value = 1, message = "Los asientos disponibles deben ser mayores a 0")
    private Integer availableSeats;
}
