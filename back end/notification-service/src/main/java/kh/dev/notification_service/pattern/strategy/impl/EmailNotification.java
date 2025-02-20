package kh.dev.notification_service.pattern.strategy.impl;

import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import jakarta.mail.internet.MimeMessage;
import kh.dev.common_util.constant.NotificationType;
import kh.dev.common_util.dto.request.NotificationRequest;
import kh.dev.common_util.util.ExecutionContext;
import kh.dev.notification_service.constant.NotificationConstant;
import kh.dev.notification_service.exception.NotificationException;
import kh.dev.notification_service.pattern.strategy.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotification implements Notification {

  private final TemplateEngine templateEngine;
  private final JavaMailSender mailSender;

  @Override
  public NotificationType getType() {
    return NotificationType.EMAIL;
  }

  // todo: handle validation
  @Override
  public boolean isValid(ExecutionContext context) {
    return true;
  }

  @Override
  public boolean send(ExecutionContext context) {
    NotificationRequest notification =
        context.get(NotificationConstant.NOTIFICATION_REQUEST, NotificationRequest.class);
    String recipient = notification.getRecipient();

    TemplateOutput template = new StringOutput();
    templateEngine.render(
        notification.getTemplate().getFileName(),
        notification.getTemplate().getParams(recipient, notification.getMetadata()),
        template);

    sendMail(recipient, notification.getTemplate().getSubject(), template.toString());
    return true;
  }

  public void sendMail(String recipient, String subject, String content) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setTo(recipient);
      helper.setSubject(subject);
      helper.setText(content, true);
      mailSender.send(mimeMessage);
    } catch (Exception e) {
      String errorMessage =
          String.format("Failed to send email to %s with subject: %s", recipient, subject);
      throw new NotificationException(errorMessage, e);
    }
  }
}
