package kz.digital.library.service;

import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.ReaderLevel;
import kz.digital.library.domain.ReadingListType;
import kz.digital.library.domain.UserAchievement;
import kz.digital.library.repository.LibraryUserRepository;
import kz.digital.library.repository.ReadingListItemRepository;
import kz.digital.library.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GamificationService {

    private final UserAchievementRepository achievementRepository;
    private final LibraryUserRepository userRepository;
    private final ReadingListItemRepository readingListItemRepository;

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

    @Transactional
    public ReaderLevel recalculateLevel(Long userId) {
        long finished = readingListItemRepository.countByUserIdAndType(userId, ReadingListType.FINISHED);
        ReaderLevel level = ReaderLevel.BRONZE;
        if (finished >= 20) {
            level = ReaderLevel.GOLD;
        } else if (finished >= 10) {
            level = ReaderLevel.SILVER;
        }
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setLevel(level);
        userRepository.save(user);
        return level;
    }

    @Transactional
    public void checkMonthlyAchievements(Long userId) {
        long finished = readingListItemRepository.findByUserId(userId).stream()
                .filter(item -> item.getType() == ReadingListType.FINISHED)
                .count();
        if (finished >= 5 && achievementRepository.findByUserId(userId).stream()
                .noneMatch(a -> "5_PER_MONTH".equals(a.getCode()))) {
            awardAchievement(userId, "5_PER_MONTH", "5 книг за месяц", "Завершил 5 книг за месяц");
        }
    }

    public Map<ReaderLevel, Long> leaderboard() {
        return userRepository.findAll().stream()
                .collect(Collectors.groupingBy(LibraryUser::getLevel, Collectors.counting()));
    }
}
