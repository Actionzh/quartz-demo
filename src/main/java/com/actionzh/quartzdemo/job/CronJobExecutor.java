package com.actionzh.quartzdemo.job;

import com.actionzh.quartzdemo.dto.ScheduleJobDTO;
import com.actionzh.quartzdemo.job.base.BaseCronJob;
import com.actionzh.quartzdemo.utils.ApplicationContextHolder;
import com.actionzh.quartzdemo.utils.JsonMapper;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;


@Deprecated
public class CronJobExecutor extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CronJobExecutor.class);

    private final static JsonMapper jsonMapper = JsonMapper.INSTANCE;
    public static final Long DEFAULT_ID = -1L;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        LOGGER.info("StaticJobFactory execute");

        ScheduleJobDTO scheduleJobDTO = jsonMapper.fromJson(context.getMergedJobDataMap().getString(BaseCronJob.JOB_PARAMS), ScheduleJobDTO.class);
        BaseCronJob job = (BaseCronJob) ApplicationContextHolder.getApplicationContext().getBean(scheduleJobDTO.getDelegateBean());
        /*if (job instanceof TenantAwareCronJob) {
            try {
                LOGGER.info("==== Start to execute tenant aware scheduleJob [{}] ===", scheduleJobDTO.getJobName());
                preHandle(scheduleJobDTO.getTenantId());
                AsyncJobHelper.setJobContext(scheduleJobDTO.getTenantId(), DEFAULT_ID, Locale.SIMPLIFIED_CHINESE.toString());
                scheduleJobService.updateStatus(scheduleJobDTO.getId(), ScheduleJob.STATUS_RUNNING);
                ((TenantAwareCronJob) job).executeInternal(context, scheduleJobDTO.getTenantId());
                LOGGER.info("==== finished workflow timer scheduleJob [{}] ===", scheduleJobDTO.getId());
                scheduleJobService.updateStatus(scheduleJobDTO.getId(), ScheduleJob.STATUS_FINISHED);
            } catch (Exception ex) {
                LOGGER.info("==== error occurred when executing workflow timer scheduleJob [{}] ===", scheduleJobDTO.getId(), ex);
                scheduleJobService.updateStatus(scheduleJobDTO.getId(), ScheduleJob.STATUS_ABORT);
                throw new AppException("Workflow timer job error!", ex);
            } finally {
                postHandle();
            }
        }*/
    }

    private void preHandle(Long tenantId) {
       /* // 设置traceId
        String traceId = LogTraceUtils.getTraceIdOrUUID();
        LogTraceUtils.beginTrace(traceId);
        // 设置APIRequestContext保证服务间调用正常
        ApiRequestContext.init().tenantId(tenantId).userId(-1L)
                .traceId(traceId)
                .locale(Locale.SIMPLIFIED_CHINESE.toString())
                .requestId(traceId).build();
        tenantResolver.setCurrentTenant(tenantId);*/
    }

    private void postHandle() {
      /*  ApiRequestContext.remove();
        LogTraceUtils.endTrace();
        tenantResolver.clear();*/
    }
}
