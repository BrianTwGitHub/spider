package com.youlin.spider.demo.service;

import com.youlin.spider.demo.vo.JobArea;
import com.youlin.spider.demo.vo.JobInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 *
 */
public interface JobService {
    /**
     * 取得工作列表
     *
     * @param jobName
     * @param jobAreaId
     * @param companyName
     * @param jobContent
     * @param pageable
     * @return
     */
    public Page<JobInfo> getJobs(String jobName, Integer jobAreaId, String companyName, String jobContent, Pageable pageable);

    /**
     * 重新取得工作內容
     *
     * @param jobId
     * @return
     */
    public JobInfo reload(Integer jobId);

    /**
     * 取得所有工作地區
     *
     * @return
     */
    public List<JobArea> getJobAreaList();
}
