package com.legend.user_service.start_up;

import com.legend.user_service.constant.ProfileConstant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Profile(ProfileConstant.NOT_TEST)
@Component
@Slf4j
public class TaskApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

  private final List<Task> tasks;

  public TaskApplicationStartup(List<Task> tasks) {
    this.tasks = tasks;
  }

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    for (Task task : tasks) {
      try {
        task.run();
        log.info("Task '{}' completed successfully.", task.getClass().getSimpleName());
      } catch (Exception e) {
        log.error(
            "Task '{}' failed with exception: {}",
            task.getClass().getSimpleName(),
            e.getMessage(),
            e);
      }
    }
  }
}
