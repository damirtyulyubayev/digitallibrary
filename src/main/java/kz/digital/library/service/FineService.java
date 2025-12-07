package kz.digital.library.service;

import kz.digital.library.domain.Fine;
import kz.digital.library.domain.Reservation;
import kz.digital.library.repository.FineRepository;
import kz.digital.library.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Fine assessFine(Long reservationId, BigDecimal amount) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        Fine fine = Fine.builder()
                .reservation(reservation)
                .user(reservation.getUser())
                .amount(amount)
                .paid(false)
                .createdAt(LocalDateTime.now())
                .build();
        return fineRepository.save(fine);
    }

    public List<Fine> finesForUser(Long userId) {
        return fineRepository.findByUserId(userId);
    }

    @Transactional
    public Fine markPaid(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found"));
        fine.setPaid(true);
        return fineRepository.save(fine);
    }

    @Transactional
    public Fine assessOverdue(Reservation reservation) {
        LocalDateTime due = reservation.getDueAt();
        LocalDateTime returned = reservation.getReturnedAt();
        if (due == null || returned == null || !returned.isAfter(due)) {
            throw new IllegalArgumentException("Reservation is not overdue");
        }
        long daysOver = Duration.between(due, returned).toDays() + 1;
        BigDecimal amount = BigDecimal.valueOf(daysOver).multiply(BigDecimal.valueOf(1.0));
        return assessFine(reservation.getId(), amount);
    }
}
