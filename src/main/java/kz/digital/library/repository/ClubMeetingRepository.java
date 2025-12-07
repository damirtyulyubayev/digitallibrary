package kz.digital.library.repository;

import kz.digital.library.domain.ClubMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMeetingRepository extends JpaRepository<ClubMeeting, Long> {
    List<ClubMeeting> findByClubId(Long clubId);
}
