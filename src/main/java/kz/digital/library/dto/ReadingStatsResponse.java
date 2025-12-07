package kz.digital.library.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadingStatsResponse {
    private long finishedThisMonth;
    private double pagesPerDay;
    private int activeGoals;
    private int achievementsCount;
}
