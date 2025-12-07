package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.ReadingListType;
import kz.digital.library.repository.BookRepository;
import kz.digital.library.repository.ReadingListItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final BookRepository bookRepository;
    private final ReadingListItemRepository readingListItemRepository;

    public List<Book> recommendForUser(Long userId) {
        // Simple history-based: pick genres from finished/reading lists and rank books by matching genre/author.
        var userFinished = readingListItemRepository.findByUserIdAndType(userId, ReadingListType.FINISHED);
        var preferredGenres = userFinished.stream()
                .map(item -> item.getBook().getGenre())
                .filter(g -> g != null && !g.isBlank())
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()));

        return bookRepository.findAll().stream()
                .sorted((a, b) -> Long.compare(
                        preferredGenres.getOrDefault(b.getGenre(), 0L),
                        preferredGenres.getOrDefault(a.getGenre(), 0L)))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Book> findSimilarBooks(Long bookId) {
        Book origin = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        String genre = origin.getGenre();
        if (genre == null || genre.isBlank()) {
            return bookRepository.findAll()
                    .stream()
                    .filter(book -> !book.getId().equals(bookId))
                    .limit(5)
                    .collect(Collectors.toList());
        }
        return bookRepository.findAll()
                .stream()
                .filter(book -> !book.getId().equals(bookId))
                .filter(book -> genre.equalsIgnoreCase(book.getGenre()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
