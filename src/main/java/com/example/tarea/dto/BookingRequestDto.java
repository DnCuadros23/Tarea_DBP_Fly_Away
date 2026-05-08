package com.example.tarea.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestDto {

    @NotNull(message = "El ID del vuelo es requerido")
    private Long flightId;
}
