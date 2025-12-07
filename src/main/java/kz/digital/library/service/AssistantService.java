package kz.digital.library.service;

import kz.digital.library.domain.Book;
import kz.digital.library.dto.ai.OpenAIChatRequest;
import kz.digital.library.dto.ai.OpenAIChatResponse;
import kz.digital.library.dto.ai.OpenAIMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final RecommendationService recommendationService;
    private final SearchService searchService;
    private final RestTemplate restTemplate;

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String openaiModel;

    public Map<String, Object> readingPlan(Long userId, String query) {
        List<Book> recs = recommendationService.recommendForUser(userId);
        List<Book> related = query != null ? searchService.search(query) : List.of();

        String aiAnswer = callOpenAI(userId, query, recs, related);

        Map<String, Object> response = new HashMap<>();
        response.put("answer", aiAnswer);
        response.put("recommendations", recs);
        response.put("related", related);
        return response;
    }

    private String callOpenAI(Long userId, String query, List<Book> recs, List<Book> related) {
        if (openaiApiKey == null || openaiApiKey.isBlank()) {
            return "AI недоступен: не задан OPENAI_API_KEY. Рекомендации сверху — базовые.";
        }

        String context = "User id: " + userId +
                "\nTop recommendations:\n" + recs.stream()
                .map(b -> "- %s by %s (%s)".formatted(b.getTitle(), b.getAuthor(), b.getGenre()))
                .collect(Collectors.joining("\n")) +
                "\nSearch results:\n" + related.stream()
                .map(b -> "- %s by %s (%s)".formatted(b.getTitle(), b.getAuthor(), b.getGenre()))
                .collect(Collectors.joining("\n"));

        OpenAIChatRequest request = OpenAIChatRequest.builder()
                .model(openaiModel)
                .temperature(0.4)
                .messages(List.of(
                        OpenAIMessage.builder().role("system").content(
                                "You are a helpful library reading assistant. " +
                                        "Suggest concise reading plan and explain why picks match user interests. " +
                                        "Always return 3-5 bullet recommendations with short rationale.")
                                .build(),
                        OpenAIMessage.builder().role("user").content(
                                "Question: %s\nContext:\n%s".formatted(
                                        query == null ? "Suggest what to read next" : query, context))
                                .build()
                ))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        HttpEntity<OpenAIChatRequest> entity = new HttpEntity<>(request, headers);
        try {
            OpenAIChatResponse response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    OpenAIChatResponse.class
            ).getBody();
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            return "AI недоступен: " + e.getMessage();
        }
        return "AI не вернул ответ.";
    }
}
