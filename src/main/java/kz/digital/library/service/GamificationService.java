package kz.digital.library.service;

import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.UserAchievement;
import kz.digital.library.repository.LibraryUserRepository;
import kz.digital.library.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamificationService {

    private final UserAchievementRepository achievementRepository;
    private final LibraryUserRepository userRepository;

    public List<UserAchievement> achievementsForUser(Long userId) {
        return achievementRepository.findByUserId(userId);
    }

    @Transactional
    public UserAchievement awardAchievement(Long userId, String code, String title, String description) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserAchievement achievement = UserAchievement.builder()
                .user(user)
                .code(code)
                .title(title)
                .description(description)
                .build();
        return achievementRepository.save(achievement);
    }

    public int currentStreak(Long userId) {
        // Placeholder for streak logic.
        return 0;
    }
}
