package com.youlin.spider.demo.service;

import com.youlin.spider.demo.entity.Company;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: xavier
 * @createdate: 2023/4/27
 */
public interface CompanyService {
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	Company saveCompany(Company company);
}
