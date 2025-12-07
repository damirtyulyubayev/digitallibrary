package kz.digital.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwardAchievementRequest {
    private String code;
    private String title;
    private String description;
}
