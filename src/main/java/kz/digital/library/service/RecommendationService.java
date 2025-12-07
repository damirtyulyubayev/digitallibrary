package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final BookRepository bookRepository;

    public List<Book> recommendForUser(Long userId) {
        // Placeholder: extend with personalized recommendations or embeddings.
        return bookRepository.findAll()
                .stream()
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
