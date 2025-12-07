package kz.digital.library.web;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.BookCopy;
import kz.digital.library.repository.BookCopyRepository;
import kz.digital.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
public class LibrarianController {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PostMapping("/books/{bookId}/copies")
    public BookCopy addCopy(@PathVariable Long bookId, @RequestBody Map<String, String> body) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        BookCopy copy = BookCopy.builder()
                .book(book)
                .inventoryCode(body.getOrDefault("inventoryCode", "INV-" + System.currentTimeMillis()))
                .available(true)
                .active(true)
                .build();
        return bookCopyRepository.save(copy);
    }

    @PostMapping("/copies/{copyId}/discard")
    public BookCopy discardCopy(@PathVariable Long copyId) {
        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new IllegalArgumentException("Copy not found"));
        copy.setActive(false);
        copy.setAvailable(false);
        return bookCopyRepository.save(copy);
    }

    @GetMapping("/books/{bookId}/copies")
    public List<BookCopy> copies(@PathVariable Long bookId) {
        return bookCopyRepository.findByBookIdAndActiveTrue(bookId);
    }
}
