package kz.digital.library.web;

import kz.digital.library.dto.LoginRequest;
import kz.digital.library.dto.RegisterRequest;
import kz.digital.library.dto.UserProfileResponse;
import kz.digital.library.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserProfileResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public UserProfileResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
