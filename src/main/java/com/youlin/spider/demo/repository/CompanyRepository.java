package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {
    Optional<Company> findCompanyByCompanyName(String companyName);
}