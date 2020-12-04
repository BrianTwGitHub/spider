package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer>, QuerydslPredicateExecutor<Company> {
    Optional<Company> findCompanyByCompanyName(String companyName);
}