package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobDao {
    /**
     * 取得工作清單
     *
     * @param jobName
     * @param jobAreaId
     * @param companyName
     * @param jobContent
     * @param jobStatus
     * @param pageable
     * @return
     */
    Page<Job> findJobByCondition(String jobName, Integer jobAreaId, String companyName, String jobContent, JobStatus jobStatus, Pageable pageable);
}
