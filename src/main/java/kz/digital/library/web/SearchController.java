package kz.digital.library.web;

import kz.digital.library.domain.Book;
import kz.digital.library.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public List<Book> search(@RequestParam("q") String query,
                             @RequestParam(value = "semantic", defaultValue = "false") boolean semantic) {
        return semantic ? searchService.semanticSearch(query) : searchService.search(query);
    }
}
