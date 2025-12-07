package kz.digital.library.web;

import kz.digital.library.domain.Book;
import kz.digital.library.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public List<Book> recommendForUser(@PathVariable Long userId) {
        return recommendationService.recommendForUser(userId);
    }

    @GetMapping("/similar/{bookId}")
    public List<Book> similarBooks(@PathVariable Long bookId) {
        return recommendationService.findSimilarBooks(bookId);
    }
}
