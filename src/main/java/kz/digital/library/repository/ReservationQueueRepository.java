package kz.digital.library.repository;

import kz.digital.library.domain.ReservationQueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationQueueRepository extends JpaRepository<ReservationQueueEntry, Long> {
    List<ReservationQueueEntry> findByBookIdOrderByPositionAsc(Long bookId);

    long countByBookId(Long bookId);
}
