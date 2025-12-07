package kz.digital.library.repository;

import kz.digital.library.domain.ReadingClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingClubRepository extends JpaRepository<ReadingClub, Long> {
}
