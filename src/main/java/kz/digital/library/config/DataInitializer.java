package kz.digital.library.config;

import kz.digital.library.domain.LibraryUser;
import kz.digital.library.domain.Role;
import kz.digital.library.repository.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final LibraryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedUsers() {
        return args -> {
            createIfMissing("admin", "admin", Role.ADMIN);
            createIfMissing("librarian", "librarian", Role.LIBRARIAN);
        };
    }

    private void createIfMissing(String username, String rawPassword, Role role) {
        userRepository.findByUsername(username).orElseGet(() -> {
            LibraryUser user = LibraryUser.builder()
                    .username(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .role(role)
                    .build();
            return userRepository.save(user);
        });
    }
}
