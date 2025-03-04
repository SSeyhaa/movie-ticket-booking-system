package kh.dev.common_util.util;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

/**
 * Utility class providing tips for working with Quartz Triggers.
 *
 * <h2>Trigger Tips</h2>
 *
 * <ul>
 *   <li><b>DateBuilder:</b> Provides a simple way to create {@link java.util.Date} objects for
 *       trigger start and end times.
 *   <li><b>TriggerBuilder:</b> Simplifies the creation of triggers with various scheduling options.
 *   <li><b>TriggerUtils:</b> Contains helper methods for analyzing triggers, such as calculating
 *       future fire times.
 * </ul>
 *
 * <h3>DateBuilder</h3>
 *
 * <p>{@link org.quartz.DateBuilder} provides utility methods to construct {@link java.util.Date}
 * instances that can be used to specify trigger start and end times.
 *
 * <h3>TriggerBuilder</h3>
 *
 * <p>{@link org.quartz.TriggerBuilder} simplifies the creation of triggers by providing various
 * methods to define schedules without directly instantiating {@link org.quartz.SimpleTrigger},
 * {@link org.quartz.CronTrigger}, or other trigger types. Instead, it allows a fluent API to
 * configure trigger parameters.
 *
 * <h3>TriggerUtils</h3>
 *
 * <p>{@link org.quartz.TriggerUtils} provides utility methods to analyze triggers, including
 * calculating future fire times and other scheduling insights.
 *
 * @see org.quartz.DateBuilder
 * @see org.quartz.TriggerBuilder
 * @see org.quartz.TriggerUtils
 */
@Component
@RequiredArgsConstructor
public class SchedulerUtils {

  private final Scheduler scheduler;

  public JobDetail buildJobDetail(
      Class<? extends Job> jobClass,
      String group,
      String jobName,
      JobDataMap dataMap,
      String description) {
    try {
      JobKey jobKey = new JobKey(jobName, group);

      if (scheduler.checkExists(jobKey)) {
        return scheduler.getJobDetail(jobKey);
      }

      return JobBuilder.newJob(jobClass)
          .withIdentity(jobName, group)
          .setJobData(dataMap != null ? dataMap : new JobDataMap())
          .withDescription(description)
          .build();
    } catch (SchedulerException e) {
      throw new kh.dev.common_util.exception.SchedulerException("Failed to build job detail", e);
    }
  }

  public void scheduleJob(
      String group,
      String triggerName,
      JobDetail jobDetail,
      JobDataMap dataMap,
      Date startAt,
      Date endAt,
      ScheduleBuilder<? extends Trigger> scheduleBuilder) {
    try {

      TriggerKey triggerKey = new TriggerKey(triggerName, group);

      Trigger trigger =
          TriggerBuilder.newTrigger()
              .withIdentity(triggerName, group)
              .forJob(jobDetail.getKey())
              .usingJobData(dataMap != null ? dataMap : new JobDataMap())
              .startAt(startAt != null ? startAt : new Date())
              .withSchedule(scheduleBuilder)
              .endAt(endAt)
              .build();

      if (scheduler.checkExists(triggerKey)) {
        scheduler.rescheduleJob(triggerKey, trigger);
      } else {
        // If the job already exists, schedule only the trigger
        if (scheduler.checkExists(jobDetail.getKey())) {
          scheduler.scheduleJob(trigger);
        } else {
          scheduler.scheduleJob(jobDetail, trigger);
        }
      }
    } catch (SchedulerException e) {
      throw new kh.dev.common_util.exception.SchedulerException("Failed to schedule job", e);
    }
  }
}
