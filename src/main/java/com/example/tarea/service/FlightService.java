package com.example.tarea.service;

import com.example.tarea.model.Flight;
import com.example.tarea.dto.*;
import com.example.tarea.exception.*;
import com.example.tarea.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public FlightResponseDto createFlight(RequestFlightDto dto) {
        // Validar que hora de salida < hora de llegada
        if (!dto.getDepartureTime().isBefore(dto.getArrivalTime())) {
            throw new BadRequestException("La hora de salida debe ser anterior a la hora de llegada");
        }

        // Validar número de vuelo único
        if (flightRepository.existsByFlightNumber(dto.getFlightNumber())) {
            throw new BadRequestException("Ya existe un vuelo con el número: " + dto.getFlightNumber());
        }

        Flight flight = Flight.builder()
                .flightNumber(dto.getFlightNumber())
                .airlineName(dto.getAirlineName())
                .origin(dto.getOrigin())
                .destination(dto.getDestination())
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getArrivalTime())
                .availableSeats(dto.getAvailableSeats())
                .build();

        Flight saved = flightRepository.save(flight);
        return mapToResponseDto(saved);
    }

    public FlightResponseDto getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con id: " + id));
        return mapToResponseDto(flight);
    }

    public List<FlightResponseDto> searchFlights(String flightNumber, String airlineName,
                                                  LocalDateTime startDate, LocalDateTime endDate) {
        return flightRepository
                .searchFlights(flightNumber, airlineName, startDate, endDate)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private FlightResponseDto mapToResponseDto(Flight flight) {
        return FlightResponseDto.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airlineName(flight.getAirlineName())
                .origin(flight.getOrigin())
                .destination(flight.getDestination())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .availableSeats(flight.getAvailableSeats())
                .build();
    }
}
