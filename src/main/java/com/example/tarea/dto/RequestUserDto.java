package com.example.tarea.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUserDto {

    @NotBlank(message = "El nombre es requerido")
    @Pattern(regexp = ".*[A-Z].*", message = "El nombre debe contener al menos una letra mayúscula")
    private String firstName;

    @NotBlank(message = "El apellido es requerido")
    @Pattern(regexp = ".*[A-Z].*", message = "El apellido debe contener al menos una letra mayúscula")
    private String lastName;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$",
             message = "La contraseña debe contener al menos 1 letra y 1 número")
    private String password;
}
