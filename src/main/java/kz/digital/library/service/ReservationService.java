package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.BookCopy;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.Reservation;
import kz.digital.library.domain.ReservationQueueEntry;
import kz.digital.library.domain.ReservationStatus;
import kz.digital.library.repository.BookCopyRepository;
import kz.digital.library.repository.LibraryUserRepository;
import kz.digital.library.repository.ReservationQueueRepository;
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
    private final NotificationService notificationService;
    private final ReservationQueueRepository queueRepository;
    private final FineService fineService;

    @Transactional
    public Reservation createReservation(Long userId, Long bookCopyId) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found"));

        if (!bookCopy.isActive()) {
            throw new IllegalStateException("Book copy is not active");
        }

        if (!bookCopy.isAvailable()) {
            throw new IllegalStateException("Book copy is not available, consider joining queue");
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
        Reservation saved = reservationRepository.save(reservation);
        notificationService.notifyUser(user, "Reservation created for copy %s, due %s".formatted(
                bookCopy.getInventoryCode(), saved.getDueAt()
        ));
        return saved;
    }

    @Transactional
    public Reservation markIssued(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(ReservationStatus.ISSUED);
        if (reservation.getDueAt() == null) {
            reservation.setDueAt(LocalDateTime.now().plusDays(DEFAULT_LOAN_DAYS));
        }
        Reservation saved = reservationRepository.save(reservation);
        notificationService.notifyUser(reservation.getUser(), "Reservation %d issued, due %s".formatted(
                reservationId, saved.getDueAt()
        ));
        return saved;
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

        Reservation saved = reservationRepository.save(reservation);
        notificationService.notifyUser(reservation.getUser(), "Reservation %d returned".formatted(reservationId));

        // If queue exists for this book, issue next reservation automatically.
        List<ReservationQueueEntry> queue = queueRepository.findByBookIdOrderByPositionAsc(bookCopy.getBook().getId());
        if (!queue.isEmpty()) {
            ReservationQueueEntry next = queue.get(0);
            queueRepository.delete(next);
            createReservation(next.getUser().getId(), bookCopy.getId());
        }

        if (reservation.getDueAt() != null && reservation.getReturnedAt() != null &&
                reservation.getReturnedAt().isAfter(reservation.getDueAt())) {
            fineService.assessOverdue(reservation);
        }
        return saved;
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

    @Transactional
    public ReservationQueueEntry enqueue(Long userId, Long bookId) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        long position = queueRepository.countByBookId(bookId) + 1;
        ReservationQueueEntry entry = ReservationQueueEntry.builder()
                .book(Book.builder().id(bookId).build())
                .user(user)
                .position((int) position)
                .createdAt(LocalDateTime.now())
                .build();
        return queueRepository.save(entry);
    }

    public List<ReservationQueueEntry> queueForBook(Long bookId) {
        return queueRepository.findByBookIdOrderByPositionAsc(bookId);
    }
}
