package com.youlin.spider.demo.service;

import com.youlin.spider.demo.vo.JobInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {
    /**
     * 取得工作列表
     *
     * @param jobName
     * @param jobAreaId
     * @param jobContent
     * @param pageable
     * @return
     */
    public Page<JobInfo> getJobs(String jobName, Integer jobAreaId, String jobContent, Pageable pageable);

    /**
     * @param jobId
     * @return
     */
    public JobInfo reload(Integer jobId);
}
