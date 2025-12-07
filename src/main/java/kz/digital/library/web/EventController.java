package kz.digital.library.web;

import kz.digital.library.domain.EventRegistration;
import kz.digital.library.domain.LibraryEvent;
import kz.digital.library.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<LibraryEvent> events() {
        return eventService.listEvents();
    }

    @PostMapping("/{id}/register")
    public EventRegistration register(@PathVariable("id") Long eventId,
                                      @RequestParam("userId") Long userId) {
        return eventService.registerUser(eventId, userId);
    }
}
