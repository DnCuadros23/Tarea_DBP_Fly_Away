package com.example.tarea.repository;

import com.example.tarea.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumber(String flightNumber);

    Optional<Flight> findByFlightNumber(String flightNumber);

    @Query("""
        SELECT f FROM Flight f
        WHERE (cast(:flightNumber as string) IS NULL OR UPPER(f.flightNumber) LIKE UPPER(CONCAT('%', cast(:flightNumber as string), '%')))
        AND   (cast(:airlineName as string) IS NULL OR UPPER(f.airlineName)  LIKE UPPER(CONCAT('%', cast(:airlineName as string),  '%')))
        AND   (cast(:startDate as timestamp) IS NULL OR f.departureTime >= :startDate)
        AND   (cast(:endDate as timestamp) IS NULL OR f.departureTime <= :endDate)
        """)
    List<Flight> searchFlights(
            @Param("flightNumber") String flightNumber,
            @Param("airlineName")  String airlineName,
            @Param("startDate")    LocalDateTime startDate,
            @Param("endDate")      LocalDateTime endDate
    );
}
