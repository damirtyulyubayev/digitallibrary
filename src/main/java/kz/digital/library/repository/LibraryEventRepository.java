package kz.digital.library.repository;

import kz.digital.library.domain.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LibraryEventRepository extends JpaRepository<LibraryEvent, Long> {
    List<LibraryEvent> findByStartAtAfterOrderByStartAtAsc(LocalDateTime dateTime);
}
