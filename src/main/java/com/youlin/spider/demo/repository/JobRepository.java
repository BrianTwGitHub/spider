package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer>, QuerydslPredicateExecutor<Job> {
    Optional<Job> findJobByJobName(String jobName);
}