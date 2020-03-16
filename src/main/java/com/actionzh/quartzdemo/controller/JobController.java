package com.actionzh.quartzdemo.controller;

import com.actionzh.quartzdemo.dto.ScheduleJob;
import com.actionzh.quartzdemo.dto.ScheduleJobDTO;
import com.actionzh.quartzdemo.job.base.BaseCronJob;
import com.actionzh.quartzdemo.service.ScheduleJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private ScheduleJobService scheduleJobService;

    @PostMapping
    @ResponseBody
    public ScheduleJobDTO create(@RequestBody ScheduleJobDTO scheduleJobDTO) {
        LOGGER.info("request create, params [{}]", scheduleJobDTO.toString());
        scheduleJobDTO.setDelegateBean(BaseCronJob.JOB_CUSTOMER_JOURNEY_TIMER);
        scheduleJobDTO.setJobGroup(BaseCronJob.STATIC_TIMER_GROUP);
        scheduleJobDTO.setStatus(ScheduleJob.STATUS_ACTIVE);
        return scheduleJobService.insert(scheduleJobDTO);
    }

    @PutMapping(value = "/{id}/inactive")
    @ResponseBody
    public void inactive(@PathVariable(value = "id") Long id) {
        LOGGER.info("request disable, id [{}]", id);
        scheduleJobService.updateStatus(id, ScheduleJob.STATUS_INACTIVE);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void delete(@PathVariable(value = "id") Long id) {
        LOGGER.info("request delete, id [{}]", id);
        scheduleJobService.delete(id);
    }

}
