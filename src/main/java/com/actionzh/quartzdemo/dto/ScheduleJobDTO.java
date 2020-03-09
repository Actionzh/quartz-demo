package com.actionzh.quartzdemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.Date;

@Data
@NoArgsConstructor
public class ScheduleJobDTO {

    private Long id;
    private Long tenantId;
    private String jobName;
    private String aliasName;
    private String jobGroup;
    private String jobTrigger;
    private String status;
    private String cronExpression;
    private DateTime timing;
    private boolean scheduled;
    private String description;

    private DateTime fromDate;
    private DateTime toDate;

    @JsonIgnore
    private Date dateCreated;
    @JsonIgnore
    private Date lastUpdated;

    /**
     * 一旦确定后不可更改，否则有兼容性问题
     */
    private String delegateBean;
    private String customerJourneyTriggerId;
    private String channelId;

}
