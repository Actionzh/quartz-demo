package com.actionzh.quartzdemo.service.impl;

import com.actionzh.quartzdemo.dto.ScheduleJob;
import com.actionzh.quartzdemo.dto.ScheduleJobDTO;
import com.actionzh.quartzdemo.job.base.BaseCronJob;
import com.actionzh.quartzdemo.orikamapper.OrikaBeanMapper;
import com.actionzh.quartzdemo.repository.ScheduleJobRepository;
import com.actionzh.quartzdemo.service.ScheduleJobService;
import com.actionzh.quartzdemo.utils.JsonMapper;
import com.actionzh.quartzdemo.utils.ScheduleUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);
    /**
     * 调度工厂Bean
     */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    protected OrikaBeanMapper beanMapper;
    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    protected TransactionTemplate transactionTemplate;
    @Resource(name = "simpleTxManager")
    protected PlatformTransactionManager transactionManager;
    protected final static JsonMapper jsonMapper = JsonMapper.INSTANCE;

    @Override
    public void afterPropertiesSet() {
        LOGGER.info(">>> init scheduleJobs");
        transactionTemplate = new TransactionTemplate(transactionManager);

        initScheduleJob();
        LOGGER.info("<<< end init scheduleJobs");
    }

    @Override
    public void initScheduleJob() {
     /*   List<ScheduleJob> scheduleJobList = scheduleJobRepository.findAllByStatus(ScheduleJob.STATUS_ACTIVE);
        for (ScheduleJob scheduleJob : scheduleJobList) {
            ScheduleJobDTO scheduleJobDTO = beanMapper.map(scheduleJob, ScheduleJobDTO.class);
            Trigger trigger = ScheduleUtils.getTrigger(scheduler, scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup());

            //不存在，创建一个
            if (trigger == null) {
                ScheduleUtils.createJob(scheduler, scheduleJobDTO);
            } else {
                //已存在，那么更新相应的定时设置
                ScheduleUtils.updateJob(scheduler, scheduleJobDTO);
            }
        }*/
    }

    @Override
    public ScheduleJobDTO insert(ScheduleJobDTO scheduleJobDTO) {
        ScheduleJob entity = beanMapper.map(scheduleJobDTO, ScheduleJob.class);
        ScheduleJob scheduleJob = scheduleJobRepository.saveAndFlush(entity);
        beanMapper.map(scheduleJob, scheduleJobDTO);
        ScheduleUtils.createJob(scheduler, scheduleJobDTO);
        return scheduleJobDTO;
    }

    @Override
    public void update(ScheduleJobDTO scheduleJobDTO) {
        ScheduleJob entity = beanMapper.map(scheduleJobDTO, ScheduleJob.class);
        ScheduleJob scheduleJob = scheduleJobRepository.saveAndFlush(entity);
        beanMapper.map(scheduleJob, scheduleJobDTO);
        ScheduleUtils.updateJob(scheduler, scheduleJobDTO);
    }

    @Override
    public void updateStatus(Long id, String status) {
        Optional<ScheduleJob> scheduleJobOpt = scheduleJobRepository.findById(id);
        if (!scheduleJobOpt.isPresent()) {
            return;
        }
        ScheduleJob scheduleJob = scheduleJobOpt.get();
        // 最终状态不允许修改了
        if (scheduleJob.getStatus().equals(ScheduleJob.STATUS_FINISHED)) {
            return;
        }
        scheduleJob.setStatus(status);

        scheduleJobRepository.saveAndFlush(scheduleJob);
        // 如果设置成inactive的删除定时器
        if (ScheduleJob.STATUS_INACTIVE.equals(status)) {
            ScheduleUtils.deleteJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        }

    }

    @Override
    public void delUpdate(ScheduleJobDTO scheduleJobDTO) {
        //数据库直接更新即可
        ScheduleJob entity = beanMapper.map(scheduleJobDTO, ScheduleJob.class);
        transactionTemplate.execute(status -> {
            ScheduleJob scheduleJob = scheduleJobRepository.saveAndFlush(entity);
            beanMapper.map(scheduleJob, scheduleJobDTO);
            //先删除
            ScheduleUtils.deleteJob(scheduler, scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup());
            //再创建
            ScheduleUtils.createJob(scheduler, scheduleJobDTO);
            return null;
        });

    }

    @Override
    public void delete(Long scheduleJobId) {
        Optional<ScheduleJob> scheduleJobOpt = scheduleJobRepository.findById(scheduleJobId);
        if (!scheduleJobOpt.isPresent()) {
            return;
        }
        ScheduleJob scheduleJob = scheduleJobOpt.get();
        transactionTemplate.execute(status -> {
            //删除数据
            scheduleJobRepository.deleteById(scheduleJobId);
            //删除运行的任务
            ScheduleUtils.deleteJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
            return null;
        });
    }

    @Override
    public void runOnce(Long scheduleJobId) {
        Optional<ScheduleJob> scheduleJobOpt = scheduleJobRepository.findById(scheduleJobId);
        if (!scheduleJobOpt.isPresent()) {
            return;
        }
        ScheduleJob scheduleJob = scheduleJobOpt.get();
        ScheduleUtils.runOnce(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    @Override
    public void pauseJob(Long scheduleJobId) {
        Optional<ScheduleJob> scheduleJobOpt = scheduleJobRepository.findById(scheduleJobId);
        if (!scheduleJobOpt.isPresent()) {
            return;
        }
        ScheduleJob scheduleJob = scheduleJobOpt.get();
        ScheduleUtils.pauseJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //演示数据库就不更新了
    }

    @Override
    public void resumeJob(Long scheduleJobId) {
        Optional<ScheduleJob> scheduleJobOpt = scheduleJobRepository.findById(scheduleJobId);
        if (!scheduleJobOpt.isPresent()) {
            return;
        }
        ScheduleJob scheduleJob = scheduleJobOpt.get();
        ScheduleUtils.resumeJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //演示数据库就不更新了
    }

    @Override
    public ScheduleJobDTO get(Long scheduleJobId) {
        Optional<ScheduleJob> scheduleJobOpt = scheduleJobRepository.findById(scheduleJobId);
        return beanMapper.map(scheduleJobOpt.orElse(null), ScheduleJobDTO.class);
    }

    @Override
    public List<ScheduleJobDTO> queryList() {

        List<ScheduleJob> scheduleJobs = scheduleJobRepository.findAll();
        List<ScheduleJobDTO> jobList = new ArrayList<>();
        try {
            for (ScheduleJob job : scheduleJobs) {
                ScheduleJobDTO scheduleJobDTO = beanMapper.map(job, ScheduleJobDTO.class);
                JobKey jobKey = ScheduleUtils.getJobKey(scheduleJobDTO.getJobName(), scheduleJobDTO.getJobGroup());
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (CollectionUtils.isEmpty(triggers)) {
                    continue;
                }

                //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
                Trigger trigger = triggers.iterator().next();
                scheduleJobDTO.setJobTrigger(trigger.getKey().getName());

                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                scheduleJobDTO.setStatus(triggerState.name());

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    scheduleJobDTO.setCronExpression(cronExpression);
                }
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return jobList;
    }

    /**
     * 获取运行中的job列表
     *
     * @return
     */
    @Override
    public List<ScheduleJobDTO> queryExecutingJobList() {
        try {
            // 存放结果集
            List<ScheduleJobDTO> jobList = new ArrayList<>();

            // 获取scheduler中的JobGroupName
            for (String group : scheduler.getJobGroupNames()) {
                // 获取JobKey 循环遍历JobKey
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(group))) {
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    JobDataMap jobDataMap = jobDetail.getJobDataMap();
                    ScheduleJobDTO scheduleJobDTO = jsonMapper.fromJson(jobDataMap.getString(BaseCronJob.JOB_PARAMS), ScheduleJobDTO.class);
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    Trigger trigger = triggers.iterator().next();
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    scheduleJobDTO.setJobTrigger(trigger.getKey().getName());
                    scheduleJobDTO.setStatus(triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        scheduleJobDTO.setCronExpression(cronExpression);
                    }
                    // 获取正常运行的任务列表
                    if (triggerState.name().equals("NORMAL")) {
                        jobList.add(scheduleJobDTO);
                    }
                }
            }
            return jobList;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }

}
