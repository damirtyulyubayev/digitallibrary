package kz.digital.library.repository;

import kz.digital.library.domain.LibraryPass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryPassRepository extends JpaRepository<LibraryPass, Long> {
    Optional<LibraryPass> findByUserId(Long userId);

    Optional<LibraryPass> findByQrToken(String qrToken);
}
