package kz.digital.library.web;

import kz.digital.library.dto.ReadingStatsResponse;
import kz.digital.library.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/reading/{userId}")
    public ReadingStatsResponse readingStats(@PathVariable Long userId) {
        return statsService.readingStats(userId);
    }

    @GetMapping("/city")
    public Map<String, Object> city() {
        return statsService.cityAnalytics();
    }
}
