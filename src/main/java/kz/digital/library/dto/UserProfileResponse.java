package kz.digital.library.dto;

import kz.digital.library.domain.ReaderLevel;
import kz.digital.library.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Role role;
    private ReaderLevel level;
}
