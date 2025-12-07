package kz.digital.library.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIChatResponse {
    private List<OpenAIChoice> choices;
}
