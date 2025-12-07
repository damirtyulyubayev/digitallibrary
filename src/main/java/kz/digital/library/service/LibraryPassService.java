package kz.digital.library.service;

import kz.digital.library.domain.LibraryPass;
import kz.digital.library.domain.LibraryUser;
import kz.digital.library.repository.LibraryPassRepository;
import kz.digital.library.repository.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LibraryPassService {

    private final LibraryPassRepository passRepository;
    private final LibraryUserRepository userRepository;

    @Transactional
    public LibraryPass generatePass(Long userId) {
        LibraryUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return passRepository.findByUserId(userId)
                .orElseGet(() -> passRepository.save(LibraryPass.builder()
                        .user(user)
                        .qrToken(UUID.randomUUID().toString())
                        .active(true)
                        .build()));
    }

    public LibraryPass findByToken(String qrToken) {
        return passRepository.findByQrToken(qrToken)
                .orElseThrow(() -> new IllegalArgumentException("Pass not found"));
    }
}
