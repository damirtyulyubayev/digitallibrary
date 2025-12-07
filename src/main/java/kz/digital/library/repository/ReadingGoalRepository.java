package kz.digital.library.repository;

import kz.digital.library.domain.ReadingGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingGoalRepository extends JpaRepository<ReadingGoal, Long> {
    List<ReadingGoal> findByUserId(Long userId);
}
