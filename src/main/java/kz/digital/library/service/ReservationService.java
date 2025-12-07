package kz.digital.library.service;

import kz.digital.library.domain.BookCopy;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.Reservation;
import kz.digital.library.domain.ReservationStatus;
import kz.digital.library.repository.BookCopyRepository;
import kz.digital.library.repository.LibraryUserRepository;
import kz.digital.library.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final int DEFAULT_LOAN_DAYS = 14;

    private final ReservationRepository reservationRepository;
    private final LibraryUserRepository userRepository;
    private final BookCopyRepository bookCopyRepository;

    @Transactional
    public Reservation createReservation(Long userId, Long bookCopyId) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));

        if (!bookCopy.isAvailable()) {
            throw new IllegalStateException("Book copy is not available");
        }

        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .user(user)
                .bookCopy(bookCopy)
                .status(ReservationStatus.REQUESTED)
                .createdAt(now)
                .dueAt(now.plusDays(DEFAULT_LOAN_DAYS))
                .build();

        bookCopy.setAvailable(false);
        bookCopyRepository.save(bookCopy);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation markIssued(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(ReservationStatus.ISSUED);
        if (reservation.getDueAt() == null) {
            reservation.setDueAt(LocalDateTime.now().plusDays(DEFAULT_LOAN_DAYS));
        }
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation markReturned(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(ReservationStatus.RETURNED);
        reservation.setReturnedAt(LocalDateTime.now());

        BookCopy bookCopy = reservation.getBookCopy();
        bookCopy.setAvailable(true);
        bookCopyRepository.save(bookCopy);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    /**
     * Simple overdue handler stub for future extensions.
     */
    @Transactional
    public void markOverdueReservations() {
        List<Reservation> overdue = reservationRepository.findByStatusAndDueAtBefore(
                ReservationStatus.ISSUED, LocalDateTime.now()
        );
        overdue.forEach(res -> {
            res.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(res);
        });
    }
}
