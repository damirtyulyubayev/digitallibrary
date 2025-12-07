package kz.digital.library.repository;

import kz.digital.library.domain.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    List<StudyGroup> findByTeacherId(Long teacherId);
}
