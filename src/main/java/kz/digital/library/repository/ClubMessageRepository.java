package kz.digital.library.repository;

import kz.digital.library.domain.ClubMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMessageRepository extends JpaRepository<ClubMessage, Long> {
    List<ClubMessage> findByClubIdOrderByCreatedAtAsc(Long clubId);
}
