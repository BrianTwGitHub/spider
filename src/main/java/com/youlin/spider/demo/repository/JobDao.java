package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobDao {
    Page<Job> findJobByCondition(String jobName, Integer jobAreaId, String companyName, String jobContent, JobStatus jobStatus, Pageable pageable);
}
