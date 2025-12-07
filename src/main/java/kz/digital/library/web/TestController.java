package kz.digital.library.web;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @Operation(summary = "Проверка API")
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
