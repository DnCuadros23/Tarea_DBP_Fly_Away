package com.example.tarea.service;

import com.example.tarea.model.*;
import com.example.tarea.dto.*;
import com.example.tarea.exception.*;
import com.example.tarea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository  flightRepository;
    private final UserRepository    userRepository;

    @Transactional
    public BookingResponseDto bookFlight(Long flightId, String userEmail) {
        // Obtener usuario autenticado
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Obtener vuelo
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con id: " + flightId));

        LocalDateTime now = LocalDateTime.now();

        // (Nice to Have) No reservar vuelos pasados o en tránsito
        if (!flight.getDepartureTime().isAfter(now)) {
            throw new BadRequestException("No se puede reservar un vuelo que ya ha despegado o está en tránsito");
        }

        // No sobrevender vuelos
        if (flight.getAvailableSeats() <= 0) {
            throw new BadRequestException("No hay asientos disponibles para este vuelo");
        }

        // (Nice to Have) Evitar conflicto de horario
        boolean conflict = bookingRepository.existsScheduleConflict(
                user.getId(), flight.getDepartureTime(), flight.getArrivalTime());
        if (conflict) {
            throw new BadRequestException("Ya tienes una reserva con conflicto de horario para este vuelo");
        }

        // Decrementar asientos disponibles
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        // Crear reserva
        Booking booking = Booking.builder()
                .user(user)
                .flight(flight)
                .customerFirstName(user.getFirstName())
                .customerLastName(user.getLastName())
                .bookingDate(now)
                .build();

        Booking saved = bookingRepository.save(booking);

        // (Nice to Have) Guardar archivo de confirmación
        generateConfirmationFile(saved);

        return mapToResponseDto(saved);
    }

    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        return mapToResponseDto(booking);
    }

    private void generateConfirmationFile(Booking booking) {
        try {
            String fileName = "flight_booking_email_" + booking.getId() + ".txt";
            DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            String content = String.format("""
                    ========================================
                    CONFIRMACION DE RESERVA - FLY AWAY TRAVEL
                    ========================================
                    Booking ID   : %d
                    Pasajero     : %s %s
                    Vuelo        : %s
                    Aerolínea    : %s
                    Origen       : %s
                    Destino      : %s
                    Salida       : %s
                    Llegada      : %s
                    Fecha reserva: %s
                    ========================================
                    """,
                    booking.getId(),
                    booking.getCustomerFirstName(),
                    booking.getCustomerLastName(),
                    booking.getFlight().getFlightNumber(),
                    booking.getFlight().getAirlineName(),
                    booking.getFlight().getOrigin(),
                    booking.getFlight().getDestination(),
                    booking.getFlight().getDepartureTime().format(fmt),
                    booking.getFlight().getArrivalTime().format(fmt),
                    booking.getBookingDate().format(fmt)
            );

            Files.writeString(Path.of(fileName), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            // No interrumpir el flujo si falla la escritura del archivo
            System.err.println("No se pudo guardar el archivo de confirmación: " + e.getMessage());
        }
    }

    private BookingResponseDto mapToResponseDto(Booking booking) {
        Flight flight = booking.getFlight();
        return BookingResponseDto.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .customerFirstName(booking.getCustomerFirstName())
                .customerLastName(booking.getCustomerLastName())
                .flightId(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airlineName(flight.getAirlineName())
                .origin(flight.getOrigin())
                .destination(flight.getDestination())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .bookingDate(booking.getBookingDate())
                .build();
    }
}
