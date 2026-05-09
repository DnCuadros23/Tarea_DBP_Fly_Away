package com.example.tarea.repository;

import com.example.tarea.model.Booking;
import com.example.tarea.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);

    // Detectar conflicto de horario: el usuario ya tiene un vuelo que se superpone
    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.user.id = :userId
        AND b.flight.departureTime < :arrivalTime
        AND b.flight.arrivalTime   > :departureTime
        """)
    boolean existsScheduleConflict(
            @Param("userId")        Long userId,
            @Param("departureTime") LocalDateTime departureTime,
            @Param("arrivalTime")   LocalDateTime arrivalTime
    );
}
