package kz.digital.library.service;

import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.Role;
import kz.digital.library.dto.UserProfileResponse;
import kz.digital.library.repository.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LibraryUserRepository userRepository;

    public UserProfileResponse getProfile(Long userId) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return AuthService.toProfile(user);
    }

    @Transactional
    public UserProfileResponse updateRole(Long userId, Role role) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        return AuthService.toProfile(userRepository.save(user));
    }
}
