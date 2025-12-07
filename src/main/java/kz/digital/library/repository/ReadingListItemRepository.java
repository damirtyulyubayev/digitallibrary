package kz.digital.library.repository;

import kz.digital.library.domain.ReadingListItem;
import kz.digital.library.domain.ReadingListType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingListItemRepository extends JpaRepository<ReadingListItem, Long> {
    List<ReadingListItem> findByUserId(Long userId);

    List<ReadingListItem> findByUserIdAndType(Long userId, ReadingListType type);

    Optional<ReadingListItem> findByUserIdAndBookId(Long userId, Long bookId);
}
