package com.actionzh.quartzdemo.job.base;


import com.actionzh.quartzdemo.utils.JsonMapper;

public interface BaseCronJob {

    String STATIC_CRON_GROUP = "LINKFLOW_STATIC_JOB_GROUP";
    String STATIC_TIMER_GROUP = "LINKFLOW_TIMER_JOB_GROUP";

    /**
     * One-time Job
     */
    String JOB_CUSTOMER_JOURNEY_TIMER = "customerJourneyTimerJob";
    String JOB_GENSEE_TIMER = "genseeTimerJob";
    String JOB_WECHAT_MASS_TIMER = "weChatMassTimerJob";

    /**
     * Cron Job
     */
    String JOB_DATA_BACKUP = "dataBackupJob";
    String JOB_DATA_BACKUP_CRON = "0 0 2 * * ?";
    String JOB_HEALTH_CHECK = "healthCheckJob";
    String JOB_HEALTH_CHECK_CRON = "0 0 1 * * ?";
    String JOB_CALC_DYNAMIC_CUSTOM_FIELD = "calcDynamicCustomFieldJob";
    String JOB_CALC_DYNAMIC_CUSTOM_FIELD_CRON = "0 40 1 * * ?";
    String JOB_DATA_CLEANER = "dataCleanerJob";
    String JOB_DATA_CLEANER_CRON = "0 15 1 * * ?";

    String JOB_UPDATE_TENANT_USED_QUOTA = "updateTenantUsedQuotaJob";
    String JOB_UPDATE_TENANT_USED_QUOTA_CROW = "0 30 1 * * ?";
    String JOB_PARAMS = "jobParams";

    JsonMapper jsonMapper = JsonMapper.INSTANCE;

}
