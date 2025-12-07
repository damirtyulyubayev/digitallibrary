package kz.digital.library.web;

import kz.digital.library.domain.BookReview;
import kz.digital.library.dto.ReviewRequest;
import kz.digital.library.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public BookReview addReview(@RequestBody ReviewRequest request) {
        return reviewService.addReview(request);
    }

    @GetMapping("/book/{bookId}")
    public List<BookReview> reviewsForBook(@PathVariable Long bookId) {
        return reviewService.getReviewsForBook(bookId);
    }
}
