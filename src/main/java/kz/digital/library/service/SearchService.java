package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookRepository bookRepository;

    public List<Book> search(String query) {
        if (!StringUtils.hasText(query)) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
                query, query, query
        );
    }

    public List<Book> semanticSearch(String query) {
        // Placeholder for pgvector/Elastic search integration.
        return search(query);
    }
}
