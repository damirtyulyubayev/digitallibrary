package kz.digital.library.repository;

import kz.digital.library.domain.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBookId(Long bookId);

    List<BookCopy> findByAvailableTrue();
}
