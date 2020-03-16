package com.actionzh.quartzdemo.service;

import com.actionzh.quartzdemo.dto.ScheduleJobDTO;

import java.util.List;

public interface ScheduleJobService {

    /**
     * Public for test, do not invoke in biz logic
     * <p>
     * 初始化定时任务
     */
    void initScheduleJob();

    /**
     * 新增
     *
     * @param scheduleJobDTO
     * @return
     */
    ScheduleJobDTO insert(ScheduleJobDTO scheduleJobDTO);

    /**
     * 直接修改 只能修改运行的时间，参数、同异步等无法修改
     *
     * @param scheduleJobDTO
     */
    void update(ScheduleJobDTO scheduleJobDTO);

    void updateStatus(Long id, String status);

    /**
     * 删除重新创建方式
     *
     * @param scheduleJobDTO
     */
    void delUpdate(ScheduleJobDTO scheduleJobDTO);

    /**
     * 删除
     *
     * @param scheduleJobId
     */
    void delete(Long scheduleJobId);

    /**
     * 运行一次任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    void runOnce(Long scheduleJobId);

    /**
     * 暂停任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    void pauseJob(Long scheduleJobId);

    /**
     * 恢复任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    void resumeJob(Long scheduleJobId);

    /**
     * 获取任务对象
     *
     * @param scheduleJobId
     * @return
     */
    ScheduleJobDTO get(Long scheduleJobId);

    /**
     * 查询任务列表
     *
     * @return
     */
    List<ScheduleJobDTO> queryList();

    /**
     * 获取运行中的任务列表
     *
     * @return
     */
    List<ScheduleJobDTO> queryExecutingJobList();
}
