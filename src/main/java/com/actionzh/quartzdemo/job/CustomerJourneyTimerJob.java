package com.actionzh.quartzdemo.job;

import com.actionzh.quartzdemo.dto.ScheduleJobDTO;
import com.actionzh.quartzdemo.job.base.BaseCronJob;
import com.actionzh.quartzdemo.job.base.TenantAwareCronJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(BaseCronJob.JOB_CUSTOMER_JOURNEY_TIMER)
public class CustomerJourneyTimerJob extends TenantAwareCronJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerJourneyTimerJob.class);

    @Override
    public void executeInternal(JobExecutionContext context, Long tenantId) {
        LOGGER.info("===== timer executor start ====");
        /*if (tenantId == null) {
            LOGGER.error("===== timer started, but cannot find tenantId ====");
            return;
        }*/
        ScheduleJobDTO scheduleJobDTO = jsonMapper.fromJson(context.getMergedJobDataMap().getString(BaseCronJob.JOB_PARAMS), ScheduleJobDTO.class);
        String triggerId = scheduleJobDTO.getCustomerJourneyTriggerId();
        LOGGER.info("===== timer executor end ====,triggerId:" + triggerId);
    }
}
