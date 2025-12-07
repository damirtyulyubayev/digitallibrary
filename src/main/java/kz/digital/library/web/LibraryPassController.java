package kz.digital.library.web;

import kz.digital.library.domain.LibraryPass;
import kz.digital.library.service.LibraryPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pass")
@RequiredArgsConstructor
public class LibraryPassController {

    private final LibraryPassService libraryPassService;

    @GetMapping("/{userId}")
    public LibraryPass generate(@PathVariable Long userId) {
        return libraryPassService.generatePass(userId);
    }

    @GetMapping("/scan/{qrToken}")
    public LibraryPass scan(@PathVariable String qrToken) {
        return libraryPassService.findByToken(qrToken);
    }
}
