package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobDao {
    /**
     * 取得工作清單
     *
     * @param userId
     * @param jobName
     * @param jobAreaIds
     * @param companyName
     * @param jobContent
     * @param statusType
     * @param isRead
     * @param isFavorite
     * @param pageable
     * @return
     */
    Page<Job> findJobByCondition(Integer userId, String jobName, List<Integer> jobAreaIds, String companyName, String jobContent, StatusType statusType, Boolean isRead, Boolean isFavorite, Pageable pageable);
}
