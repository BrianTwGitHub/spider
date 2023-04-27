package com.youlin.spider.demo.service.impl;

import com.youlin.spider.demo.entity.Company;
import com.youlin.spider.demo.repository.CompanyRepository;
import com.youlin.spider.demo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: xavier
 * @createdate: 2023/4/27
 */
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private final CompanyRepository companyRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Company saveCompany(Company company) {
		return this.companyRepository.saveAndFlush(company);
	}

}
