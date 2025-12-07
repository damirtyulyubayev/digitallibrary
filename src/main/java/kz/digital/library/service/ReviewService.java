package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.BookReview;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.dto.ReviewRequest;
import kz.digital.library.repository.BookRepository;
import kz.digital.library.repository.BookReviewRepository;
import kz.digital.library.repository.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookReviewRepository reviewRepository;
    private final LibraryUserRepository userRepository;
    private final BookRepository bookRepository;

    public BookReview addReview(ReviewRequest request) {
        LibraryUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        BookReview review = BookReview.builder()
                .user(user)
                .book(book)
                .rating(request.getRating())
                .text(request.getText())
                .createdAt(LocalDateTime.now())
                .build();
        return reviewRepository.save(review);
    }

    public List<BookReview> getReviewsForBook(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }
}
