package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, QuerydslPredicateExecutor<Job> {
	Optional<Job> findJobByJobNameAndCompany_CompanyName(String jobName, String companyName);
}