package kz.digital.library.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIChatRequest {
    private String model;
    private List<OpenAIMessage> messages;
    private double temperature;
}
