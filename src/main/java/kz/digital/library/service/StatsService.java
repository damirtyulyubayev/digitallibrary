package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.LibraryBranch;
import kz.digital.library.repository.BookRepository;
import kz.digital.library.repository.LibraryBranchRepository;
import kz.digital.library.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;
    private final LibraryBranchRepository branchRepository;

    public List<Book> popularBooks() {
        // Placeholder: would aggregate reservations and reviews for popularity.
        return bookRepository.findAll()
                .stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    public Map<String, Object> kpis() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalBooks", bookRepository.count());
        metrics.put("totalReservations", reservationRepository.count());
        metrics.put("branches", branchRepository.count());
        return metrics;
    }

    public List<LibraryBranch> branches() {
        return branchRepository.findAll();
    }
}
