package com.actionzh.quartzdemo.dto;

import com.actionzh.quartzdemo.domain.BaseEntity;
import com.actionzh.quartzdemo.utils.date.JodaTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "schedule_job")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
/**
 * 不需要管理Tenant，因为有跨tenant操作
 */
public class ScheduleJob extends BaseEntity {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_FINISHED = "FINISHED";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String STATUS_ABORT = "ABORT";
    private static final long serialVersionUID = 5455755049973038374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;
    private String jobName;
    private String aliasName;
    private String jobGroup;
    private String jobTrigger;
    private String status;
    private String cronExpression;

    @Convert(converter = JodaTimeConverter.class)
    private DateTime timing;
    @Convert(converter = JodaTimeConverter.class)
    private DateTime fromDate;
    @Convert(converter = JodaTimeConverter.class)
    private DateTime toDate;
    private Boolean scheduled;
    private String description;
    private String delegateBean;
    private String customerJourneyTriggerId;
}
