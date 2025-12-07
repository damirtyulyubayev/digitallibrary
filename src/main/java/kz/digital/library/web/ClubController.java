package kz.digital.library.web;

import kz.digital.library.domain.ClubMeeting;
import kz.digital.library.domain.ClubMessage;
import kz.digital.library.domain.ReadingClub;
import kz.digital.library.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    public ReadingClub create(@RequestBody Map<String, String> body) {
        return clubService.createClub(body.get("name"), body.get("description"), Long.valueOf(body.get("ownerId")));
    }

    @GetMapping
    public List<ReadingClub> list() {
        return clubService.allClubs();
    }

    @PostMapping("/{id}/meetings")
    public ClubMeeting addMeeting(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return clubService.addMeeting(id, body.get("title"), LocalDateTime.parse(body.get("scheduledAt")), body.get("location"));
    }

    @GetMapping("/{id}/meetings")
    public List<ClubMeeting> meetings(@PathVariable Long id) {
        return clubService.meetings(id);
    }

    @PostMapping("/{id}/messages")
    public ClubMessage postMessage(@PathVariable Long id, @RequestParam Long userId, @RequestParam String content) {
        return clubService.postMessage(id, userId, content);
    }

    @GetMapping("/{id}/messages")
    public List<ClubMessage> messages(@PathVariable Long id) {
        return clubService.messages(id);
    }
}
