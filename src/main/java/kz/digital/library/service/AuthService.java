package kz.digital.library.service;

import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.Role;
import kz.digital.library.dto.LoginRequest;
import kz.digital.library.dto.RegisterRequest;
import kz.digital.library.dto.UserProfileResponse;
import kz.digital.library.repository.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final LibraryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserProfileResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        LibraryUser user = LibraryUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.READER)
                .build();
        userRepository.save(user);
        return toProfile(user);
    }

    public UserProfileResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        LibraryUserDetails principal = (LibraryUserDetails) authentication.getPrincipal();
        LibraryUser user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toProfile(user);
    }

    public static UserProfileResponse toProfile(LibraryUser user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
