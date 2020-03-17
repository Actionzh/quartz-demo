package com.actionzh.quartzdemo.job.base;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public abstract class TenantAwareCronJob implements Job, BaseCronJob {

    public abstract void executeInternal(JobExecutionContext context, Long tenantId);

    @Override
    public void execute(JobExecutionContext context) {
    }
}