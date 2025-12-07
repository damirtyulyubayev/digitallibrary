package kz.digital.library.service;

import kz.digital.library.domain.EventRegistration;
import kz.digital.library.domain.LibraryEvent;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.repository.EventRegistrationRepository;
import kz.digital.library.repository.LibraryEventRepository;
import kz.digital.library.repository.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final LibraryEventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final LibraryUserRepository userRepository;

    public List<LibraryEvent> listEvents() {
        List<LibraryEvent> upcoming = eventRepository.findByStartAtAfterOrderByStartAtAsc(LocalDateTime.now());
        if (upcoming.isEmpty()) {
            return eventRepository.findAll();
        }
        return upcoming;
    }

    @Transactional
    public EventRegistration registerUser(Long eventId, Long userId) {
        LibraryEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalStateException("User already registered for event");
        }

        if (event.getCapacity() != null) {
            long registeredCount = registrationRepository.countByEventId(eventId);
            if (registeredCount >= event.getCapacity()) {
                throw new IllegalStateException("Event is at capacity");
            }
        }

        EventRegistration registration = EventRegistration.builder()
                .event(event)
                .user(user)
                .registeredAt(LocalDateTime.now())
                .build();
        return registrationRepository.save(registration);
    }
}
