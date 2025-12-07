package kz.digital.library.repository;

import kz.digital.library.domain.LibraryBranch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryBranchRepository extends JpaRepository<LibraryBranch, Long> {
}
