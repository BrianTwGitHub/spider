package com.youlin.spider.demo.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.entity.QJob;
import com.youlin.spider.demo.enums.StatusType;
import com.youlin.spider.demo.repository.JobDao;
import com.youlin.spider.demo.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JobDaoImpl implements JobDao {

    private final JobRepository jobRepository;

    @Override
    public Page<Job> findJobByCondition(Integer userId, String jobName, List<Integer> jobAreaIds, String companyName, String jobContent, StatusType statusType, Boolean isRead, Boolean isFavorite, Pageable pageable) {
        QJob qJob = QJob.job;

        BooleanExpression conditions = Expressions.asBoolean(true).isTrue();

        if (userId != null) {
            conditions = conditions.and(qJob.userId.eq(userId));
        }

        if (StringUtils.hasLength(jobName)) {
            conditions = conditions.and(qJob.jobName.containsIgnoreCase(jobName));
        }

        if (!CollectionUtils.isEmpty(jobAreaIds)) {
            conditions = conditions.and(qJob.area.id.in(jobAreaIds));
        }

        if (StringUtils.hasLength(companyName)) {
            conditions = conditions.and(qJob.company.companyName.containsIgnoreCase(companyName));
        }

        if (StringUtils.hasLength(jobContent)) {
            conditions = conditions.and(qJob.jobContent.containsIgnoreCase(jobContent));
        }

        if (statusType != null) {
            conditions = conditions.and(qJob.status.eq(statusType));
        }

        conditions = conditions.and(qJob.status.ne(StatusType.DELETED));

        if (isRead != null) {
            conditions = conditions.and(qJob.isRead.eq(isRead));
        }

        if (isFavorite != null) {
            conditions = conditions.and(qJob.isFavorite.eq(isFavorite));
        }
        Sort sort = pageable.getSort();
        List<Sort.Order> newOrders = new ArrayList<>();

        for (Sort.Order order : sort) {
            String propertyName = order.getProperty();
            Sort.Direction direction = order.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC;
            if (order.getProperty().equalsIgnoreCase("jobArea")) {
                propertyName = "area.id";
            } else if (order.getProperty().equalsIgnoreCase("jobCompany")) {
                propertyName = "company.id";
            }
            newOrders.add(Sort.Order.by(propertyName).with(direction));
        }

        newOrders.add(Sort.Order.desc("jobUpdateDate"));

        return jobRepository.findAll(conditions, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(newOrders)));
    }

}
