package kz.digital.library.web;

import kz.digital.library.domain.UserAchievement;
import kz.digital.library.dto.AwardAchievementRequest;
import kz.digital.library.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    @GetMapping("/{userId}/achievements")
    public List<UserAchievement> achievements(@PathVariable Long userId) {
        return gamificationService.achievementsForUser(userId);
    }

    @PostMapping("/{userId}/award")
    public UserAchievement award(@PathVariable Long userId, @RequestBody AwardAchievementRequest request) {
        return gamificationService.awardAchievement(userId, request.getCode(), request.getTitle(), request.getDescription());
    }
}
