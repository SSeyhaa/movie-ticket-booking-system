package kh.dev.notification_service.controller;

import kh.dev.common_util.dto.request.NotificationRequest;
import kh.dev.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @PostMapping
  public ResponseEntity<Void> send(@RequestBody NotificationRequest notification) {
    notificationService.send(notification);
    return ResponseEntity.noContent().build();
  }
}
