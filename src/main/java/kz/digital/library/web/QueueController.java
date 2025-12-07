package kz.digital.library.web;

import kz.digital.library.domain.ReservationQueueEntry;
import kz.digital.library.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations/queue")
@RequiredArgsConstructor
public class QueueController {

    private final ReservationService reservationService;

    @PostMapping("/book/{bookId}")
    public ReservationQueueEntry enqueue(@PathVariable Long bookId, @RequestParam Long userId) {
        return reservationService.enqueue(userId, bookId);
    }

    @GetMapping("/book/{bookId}")
    public List<ReservationQueueEntry> queue(@PathVariable Long bookId) {
        return reservationService.queueForBook(bookId);
    }
}
