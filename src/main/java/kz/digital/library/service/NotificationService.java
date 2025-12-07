package kz.digital.library.service;

import kz.digital.library.domain.LibraryUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notifyUser(LibraryUser user, String message) {
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            sendEmail(user.getEmail(), "Library notification", message);
        } else if (user.getPhone() != null && !user.getPhone().isBlank()) {
            sendSms(user.getPhone(), message);
        } else {
            log.info("Notification (fallback) to {}: {}", user.getUsername(), message);
        }
    }

    public void sendEmail(String to, String subject, String body) {
        log.info("Email to {} | {} | {}", to, subject, body);
    }

    public void sendSms(String phone, String body) {
        log.info("SMS to {} | {}", phone, body);
    }
}
