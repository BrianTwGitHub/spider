package com.youlin.spider.demo.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.entity.QJob;
import com.youlin.spider.demo.enums.JobStatus;
import com.youlin.spider.demo.repository.JobDao;
import com.youlin.spider.demo.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JobDaoImpl implements JobDao {

    private final JobRepository jobRepository;

    @Override
    public Page<Job> findJobByCondition(String jobName, Integer jobAreaId, String jobContent, JobStatus jobStatus, Pageable pageable) {
        QJob qJob = QJob.job;

        BooleanExpression conditions = Expressions.asBoolean(true).isTrue();

        if (StringUtils.hasLength(jobName)) {
            conditions.and(qJob.jobName.likeIgnoreCase(jobName));
        }

        if (jobAreaId != null) {
            conditions.and(qJob.area.id.eq(jobAreaId));
        }

        if (StringUtils.hasLength(jobContent)) {
            conditions.and(qJob.jobContent.likeIgnoreCase(jobContent));
        }

        if (jobStatus != null) {
            conditions.and(qJob.status.ne(JobStatus.DELETE));
        }

        return jobRepository.findAll(conditions, pageable);
    }

}
