package kz.digital.library.repository;

import kz.digital.library.domain.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    List<ReadingProgress> findByUserId(Long userId);

    Optional<ReadingProgress> findByUserIdAndBookId(Long userId, Long bookId);
}
