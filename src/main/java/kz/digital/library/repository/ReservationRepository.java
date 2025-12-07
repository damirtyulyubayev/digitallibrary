package kz.digital.library.repository;

import kz.digital.library.domain.Reservation;
import kz.digital.library.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByStatusAndDueAtBefore(ReservationStatus status, LocalDateTime time);
}
