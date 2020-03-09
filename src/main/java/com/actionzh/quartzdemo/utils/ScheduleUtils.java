package com.actionzh.quartzdemo.utils;

import com.actionzh.quartzdemo.dto.ScheduleJobDTO;
import com.actionzh.quartzdemo.job.CronJobExecutor;
import com.actionzh.quartzdemo.job.base.BaseCronJob;
import org.joda.time.DateTime;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleUtils.class);

    private final static JsonMapper jsonMapper = JsonMapper.INSTANCE;

    /**
     * 获取触发器key
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static TriggerKey getTriggerKey(String jobName, String jobGroup) {

        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduler the scheduler
     * @param jobName   the job name
     * @param jobGroup  the job group
     * @return cron trigger
     */
    @SuppressWarnings("unchecked")
    public static <T extends Trigger> T getTrigger(Scheduler scheduler, String jobName, String jobGroup) {

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            return (T) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            LOGGER.error("获取定时任务CronTrigger出现异常", e);
            throw new RuntimeException("获取定时任务CronTrigger出现异常");
        }
    }

    public static List<Trigger> getTriggers(Scheduler scheduler, String jobGroup) {
        try {
            List<Trigger> triggers = new ArrayList<>();
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup))) {
                triggers.addAll(scheduler.getTriggersOfJob(jobKey));
            }
            return triggers;
        } catch (SchedulerException e) {
            LOGGER.error("获取静态Trigger列表出现异常", e);
            throw new RuntimeException("获取静态Trigger列表出现异常");
        }
    }

    public static void createJob(Scheduler scheduler, ScheduleJobDTO scheduleJobDTO) {
        // 先删除存在的的
        deleteJob(scheduler, scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup());
        if (scheduleJobDTO.isScheduled()) {
            createScheduleJob(scheduler, scheduleJobDTO);
        } else {
            createNoRepeatJob(scheduler, scheduleJobDTO);
        }
    }

    /**
     * 创建任务，请使用elastic-job
     *
     * @param scheduler      the scheduler
     * @param scheduleJobDTO the schedule job
     */
    @Deprecated
    public static void createScheduleJob(Scheduler scheduler, ScheduleJobDTO scheduleJobDTO) {
        createScheduleJob(scheduler, scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup(),
                scheduleJobDTO.getCronExpression(), scheduleJobDTO);
    }

    /**
     * 创建定时任务, 请使用elastic-job
     *
     * @param scheduler      the scheduler
     * @param jobName        the job name
     * @param jobGroup       the job group
     * @param cronExpression the cron expression
     * @param scheduleJobDTO the param
     */
    @Deprecated
    private static void createScheduleJob(Scheduler scheduler, String jobName, String jobGroup,
                                          String cronExpression, ScheduleJobDTO scheduleJobDTO) {

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(CronJobExecutor.class).withIdentity(jobName, jobGroup).build();

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        Date start = scheduleJobDTO.getFromDate() == null ? DateTime.now().toDate() : scheduleJobDTO.getFromDate().toDate();
        Date end = scheduleJobDTO.getToDate() == null ? null : scheduleJobDTO.getToDate().toDate();
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(scheduleBuilder)
                .startAt(start)
                .endAt(end)
                .build();

        String jobTrigger = trigger.getKey().getName();

        scheduleJobDTO.setJobTrigger(jobTrigger);

        //放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(BaseCronJob.JOB_PARAMS, jsonMapper.toJson(scheduleJobDTO));

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOGGER.error("创建定时任务失败", e);
            throw new RuntimeException("创建定时任务失败", e);
        }
    }

    private static void createNoRepeatJob(Scheduler scheduler, ScheduleJobDTO scheduleJobDTO) {
        createNoRepeatJob(scheduler, scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup(),
                scheduleJobDTO.getTiming(), scheduleJobDTO);
    }

    private static void createNoRepeatJob(Scheduler scheduler, String jobName, String jobGroup,
                                          DateTime startTime, ScheduleJobDTO scheduleJobDTO) {

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(CronJobExecutor.class).withIdentity(jobName, jobGroup).build();

        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).startAt(startTime.toDate()).build();

        String jobTrigger = trigger.getKey().getName();

        scheduleJobDTO.setJobTrigger(jobTrigger);

        //放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(BaseCronJob.JOB_PARAMS, jsonMapper.toJson(scheduleJobDTO));

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOGGER.error("创建不重复任务失败", e);
            throw new RuntimeException("创建不重复任务失败", e);
        }
    }

    /**
     * 运行一次任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void runOnce(Scheduler scheduler, String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            LOGGER.error("运行一次定时任务失败", e);
            throw new RuntimeException("运行一次定时任务失败");
        }
    }

    /**
     * 暂停任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            LOGGER.error("暂停定时任务失败", e);
            throw new RuntimeException("暂停定时任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            LOGGER.error("暂停定时任务失败", e);
            throw new RuntimeException("暂停定时任务失败");
        }
    }

    /**
     * 获取jobKey
     *
     * @param jobName  the job name
     * @param jobGroup the job group
     * @return the job key
     */
    public static JobKey getJobKey(String jobName, String jobGroup) {

        return JobKey.jobKey(jobName, jobGroup);
    }

    public static void updateJob(Scheduler scheduler, ScheduleJobDTO scheduleJobDTO) {
        if (scheduleJobDTO.isScheduled()) {
            updateScheduleJob(scheduler, scheduleJobDTO);
        } else {
            updateNoRepeatJob(scheduler, scheduleJobDTO);
        }
    }

    /**
     * 更新定时任务
     *
     * @param scheduler      the scheduler
     * @param scheduleJobDTO the schedule job
     */
    public static void updateScheduleJob(Scheduler scheduler, ScheduleJobDTO scheduleJobDTO) {
        updateScheduleJob(scheduler, scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup(),
                scheduleJobDTO.getCronExpression(), scheduleJobDTO);
    }

    /**
     * 更新定时任务
     *
     * @param scheduler      the scheduler
     * @param jobName        the job name
     * @param jobGroup       the job group
     * @param cronExpression the cron expression
     * @param scheduleJobDTO the param
     */
    private static void updateScheduleJob(Scheduler scheduler, String jobName, String jobGroup,
                                          String cronExpression, ScheduleJobDTO scheduleJobDTO) {

        try {
            TriggerKey triggerKey = ScheduleUtils.getTriggerKey(jobName, jobGroup);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

            //构建job信息
//            JobDetail jobDetail = JobBuilder.newJob(CronJobExecutor.class).withIdentity(jobName, jobGroup).build();
//            jobDetail.getJobDataMap().put(BaseCronJob.JOB_PARAMS, jsonMapper.toJson(scheduleJobDTO));
//            scheduler.addJob(jobDetail, true);

            // 忽略状态为PAUSED的任务，解决集群环境中在其他机器设置定时任务为PAUSED状态后，集群环境启动另一台主机时定时任务全被唤醒的bug
            if (!triggerState.name().equalsIgnoreCase("PAUSED")) {
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            LOGGER.error("更新定时任务失败", e);
            throw new RuntimeException("更新定时任务失败");
        }
    }

    private static void updateNoRepeatJob(Scheduler scheduler, ScheduleJobDTO scheduleJobDTO) {
        updateNoRepeatJob(scheduler, scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup(),
                scheduleJobDTO.getTiming(), scheduleJobDTO);
    }

    private static void updateNoRepeatJob(Scheduler scheduler, String jobName, String jobGroup,
                                          DateTime startTime, ScheduleJobDTO scheduleJobDTO) {

        try {
            TriggerKey triggerKey = ScheduleUtils.getTriggerKey(jobName, jobGroup);

            SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).startAt(startTime.toDate()).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

            // 忽略状态为PAUSED的任务，解决集群环境中在其他机器设置定时任务为PAUSED状态后，集群环境启动另一台主机时定时任务全被唤醒的bug
            if (!triggerState.name().equalsIgnoreCase("PAUSED")) {
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            LOGGER.error("更新定时任务失败", e);
            throw new RuntimeException("更新定时任务失败");
        }
    }

    /**
     * 删除定时任务
     *
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void deleteJob(Scheduler scheduler, String jobName, String jobGroup) {
        try {
            JobKey jobKey = getJobKey(jobName, jobGroup);
            if (scheduler.getJobDetail(jobKey) != null) {
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            LOGGER.error("删除定时任务失败", e);
            throw new RuntimeException("删除定时任务失败");
        }
    }

}
