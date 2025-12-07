package kz.digital.library.web;

import kz.digital.library.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

    @GetMapping("/plan")
    public Map<String, Object> plan(@RequestParam Long userId,
                                    @RequestParam(required = false) String query) {
        return assistantService.readingPlan(userId, query);
    }
}
