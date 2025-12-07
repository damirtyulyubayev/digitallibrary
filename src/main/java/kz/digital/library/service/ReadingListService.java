package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.ReadingListItem;
import kz.digital.library.domain.ReadingListType;
import kz.digital.library.repository.BookRepository;
import kz.digital.library.repository.LibraryUserRepository;
import kz.digital.library.repository.ReadingListItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingListService {

    private final ReadingListItemRepository readingListRepository;
    private final LibraryUserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ReadingListItem addToReadingList(Long userId, Long bookId, ReadingListType type) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        ReadingListItem item = readingListRepository.findByUserIdAndBookId(userId, bookId)
                .orElse(ReadingListItem.builder().user(user).book(book).build());
        item.setType(type);
        return readingListRepository.save(item);
    }

    public List<ReadingListItem> getReadingList(Long userId, ReadingListType type) {
        if (type == null) {
            return readingListRepository.findByUserId(userId);
        }
        return readingListRepository.findByUserIdAndType(userId, type);
    }
}
