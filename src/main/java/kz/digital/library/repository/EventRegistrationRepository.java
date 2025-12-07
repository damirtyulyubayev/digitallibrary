package kz.digital.library.repository;

import kz.digital.library.domain.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByEventId(Long eventId);

    long countByEventId(Long eventId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
