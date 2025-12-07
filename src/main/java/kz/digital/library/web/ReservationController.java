package kz.digital.library.web;

import kz.digital.library.domain.Reservation;
import kz.digital.library.dto.ReservationRequest;
import kz.digital.library.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public Reservation create(@RequestBody ReservationRequest request) {
        return reservationService.createReservation(request.getUserId(), request.getBookCopyId());
    }

    @PostMapping("/{id}/issue")
    public Reservation issue(@PathVariable Long id) {
        return reservationService.markIssued(id);
    }

    @PostMapping("/{id}/return")
    public Reservation returnBook(@PathVariable Long id) {
        return reservationService.markReturned(id);
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> byUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }
}
