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

    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "job_name")
    private String jobName;
    @Column(name = "alias_name")
    private String aliasName;
    @Column(name = "job_group")
    private String jobGroup;
    @Column(name = "job_trigger")
    private String jobTrigger;
    @Column(name = "status")
    private String status;
    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "timing")
    @Convert(converter = JodaTimeConverter.class)
    private DateTime timing;
    @Column(name = "from_date")
    @Convert(converter = JodaTimeConverter.class)
    private DateTime fromDate;
    @Column(name = "to_date")
    @Convert(converter = JodaTimeConverter.class)
    private DateTime toDate;
    @Column(name = "scheduled")
    private Boolean scheduled;
    @Column(name = "description")
    private String description;
    @Column(name = "delegate_bean")
    private String delegateBean;
    @Column(name = "customer_journey_trigger_id")
    private String customerJourneyTriggerId;
}
