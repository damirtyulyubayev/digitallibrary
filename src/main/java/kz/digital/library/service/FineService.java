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
}
