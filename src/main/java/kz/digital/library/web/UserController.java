package kz.digital.library.web;

import kz.digital.library.domain.Role;
import kz.digital.library.dto.UserProfileResponse;
import kz.digital.library.service.LibraryUserDetails;
import kz.digital.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponse me(@AuthenticationPrincipal LibraryUserDetails principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Not authenticated");
        }
        return userService.getProfile(principal.getId());
    }

    @GetMapping("/{id}")
    public UserProfileResponse getById(@PathVariable Long id) {
        return userService.getProfile(id);
    }

    @PatchMapping("/{id}/role")
    public UserProfileResponse updateRole(@PathVariable Long id, @RequestParam Role role) {
        return userService.updateRole(id, role);
    }
}
