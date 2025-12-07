package kz.digital.library.dto.ai;

import lombok.Data;

@Data
public class OpenAIChoice {
    private int index;
    private OpenAIMessage message;
}
