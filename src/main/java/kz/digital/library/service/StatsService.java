package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.LibraryBranch;
import kz.digital.library.domain.ReadingGoal;
import kz.digital.library.domain.ReadingProgress;
import kz.digital.library.dto.ReadingStatsResponse;
import kz.digital.library.repository.BookRepository;
import kz.digital.library.repository.LibraryBranchRepository;
import kz.digital.library.repository.ReadingGoalRepository;
import kz.digital.library.repository.ReadingProgressRepository;
import kz.digital.library.repository.ReservationRepository;
import kz.digital.library.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final ReadingProgressRepository readingProgressRepository;
    private final ReadingGoalRepository readingGoalRepository;
    private final UserAchievementRepository userAchievementRepository;

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

    public ReadingStatsResponse readingStats(Long userId) {
        List<ReadingProgress> progress = readingProgressRepository.findByUserId(userId);
        double pagesPerDay = progress.stream()
                .mapToInt(p -> p.getCurrentPage() == null ? 0 : p.getCurrentPage())
                .average()
                .orElse(0.0);

        LocalDate now = LocalDate.now();
        long finishedThisMonth = progress.stream()
                .filter(p -> p.getLastUpdated() != null && p.getLastUpdated().toLocalDate().getMonth().equals(now.getMonth()))
                .count();

        List<ReadingGoal> goals = readingGoalRepository.findByUserId(userId);
        int activeGoals = (int) goals.stream()
                .filter(g -> g.getPeriodEnd() != null && !g.getPeriodEnd().isBefore(now))
                .count();

        int achievementsCount = (int) userAchievementRepository.findByUserId(userId).size();

        return ReadingStatsResponse.builder()
                .finishedThisMonth(finishedThisMonth)
                .pagesPerDay(pagesPerDay)
                .activeGoals(activeGoals)
                .achievementsCount(achievementsCount)
                .build();
    }

    public Map<String, Object> cityAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("branches", branchRepository.count());
        analytics.put("books", bookRepository.count());
        Map<String, Long> byGenre = bookRepository.findAll().stream()
                .filter(b -> b.getGenre() != null)
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
        analytics.put("genres", byGenre);
        return analytics;
    }
}
