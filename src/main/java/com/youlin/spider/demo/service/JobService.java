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
     * @param userId
     * @param jobName
     * @param jobAreaIds
     * @param companyName
     * @param jobContent
     * @param isRead
     * @param isFavorite
     * @param pageable
     * @return
     */
    Page<JobInfo> getJobs(Integer userId, String jobName, List<Integer> jobAreaIds, String companyName, String jobContent, Boolean isRead, Boolean isFavorite, Pageable pageable);

    /**
     * 重新取得工作內容
     *
     * @param jobId
     * @return
     */
    JobInfo reload(Integer jobId);

    /**
     * 重新取得工作內容
     *
     * @return
     */
    List<JobInfo> failedReload();

    /**
     * 取得所有工作地區
     *
     * @return
     */
    List<JobArea> getJobAreaList();

    /**
     * 設定Job已讀
     *
     * @param jobId
     */
    void readJob(Integer jobId);

    /**
     * 加入/取消我的最愛
     *
     * @param jobId
     */
    boolean favoriteJob(Integer jobId);

    /**
     * 重新取得公司連結
     */
    void reloadCompanyUrl();
}
