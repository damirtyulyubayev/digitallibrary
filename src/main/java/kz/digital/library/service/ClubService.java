package kz.digital.library.service;

import kz.digital.library.domain.ClubMeeting;
import kz.digital.library.domain.ClubMessage;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.ReadingClub;
import kz.digital.library.repository.ClubMeetingRepository;
import kz.digital.library.repository.ClubMessageRepository;
import kz.digital.library.repository.LibraryUserRepository;
import kz.digital.library.repository.ReadingClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ReadingClubRepository clubRepository;
    private final LibraryUserRepository userRepository;
    private final ClubMeetingRepository meetingRepository;
    private final ClubMessageRepository messageRepository;

    @Transactional
    public ReadingClub createClub(String name, String description, Long ownerId) {
        LibraryUser owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        ReadingClub club = ReadingClub.builder()
                .name(name)
                .description(description)
                .owner(owner)
                .build();
        return clubRepository.save(club);
    }

    public List<ReadingClub> allClubs() {
        return clubRepository.findAll();
    }

    @Transactional
    public ClubMeeting addMeeting(Long clubId, String title, LocalDateTime when, String location) {
        ReadingClub club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        ClubMeeting meeting = ClubMeeting.builder()
                .club(club)
                .title(title)
                .scheduledAt(when)
                .location(location)
                .build();
        return meetingRepository.save(meeting);
    }

    public List<ClubMeeting> meetings(Long clubId) {
        return meetingRepository.findByClubId(clubId);
    }

    @Transactional
    public ClubMessage postMessage(Long clubId, Long userId, String content) {
        ReadingClub club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ClubMessage msg = ClubMessage.builder()
                .club(club)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return messageRepository.save(msg);
    }

    public List<ClubMessage> messages(Long clubId) {
        return messageRepository.findByClubIdOrderByCreatedAtAsc(clubId);
    }
}
