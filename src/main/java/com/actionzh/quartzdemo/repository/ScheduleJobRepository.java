package com.actionzh.quartzdemo.repository;

import com.actionzh.quartzdemo.dto.ScheduleJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(transactionManager = "simpleJpaTxManager", rollbackFor = Exception.class, readOnly = true)
public interface ScheduleJobRepository extends JpaRepository<ScheduleJob, Long> {

    List<ScheduleJob> findAllByStatus(String status);
}
