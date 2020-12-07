package com.youlin.spider.demo.service.impl;

import com.youlin.spider.demo.entity.Area;
import com.youlin.spider.demo.entity.Company;
import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.entity.QArea;
import com.youlin.spider.demo.enums.JobStatus;
import com.youlin.spider.demo.repository.AreaRepository;
import com.youlin.spider.demo.repository.CompanyRepository;
import com.youlin.spider.demo.repository.JobDao;
import com.youlin.spider.demo.repository.JobRepository;
import com.youlin.spider.demo.service.JobService;
import com.youlin.spider.demo.service.ProcessJobInfoService;
import com.youlin.spider.demo.vo.JobArea;
import com.youlin.spider.demo.vo.JobInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobDao jobDao;

    private final JobRepository jobRepository;

    private final AreaRepository areaRepository;

    private final CompanyRepository companyRepository;

    private final ProcessJobInfoService processJobInfoService;

    @Override
    public Page<JobInfo> getJobs(String jobName, Integer jobAreaId, String companyName, String jobContent, Pageable pageable) {
        Page<Job> jobByJobNameLike = jobDao.findJobByCondition(jobName, jobAreaId, companyName, jobContent, JobStatus.DELETE, pageable);
        if (!jobByJobNameLike.isEmpty()) {
            List<Area> areaList = areaRepository.findAll();
            List<Company> companyList = companyRepository.findAll();
            List<JobInfo> jobInfoList = jobByJobNameLike.getContent().stream()
                    .map(job -> JobInfo.builder()
                            .jobId(job.getId())
                            .jobName(job.getJobName())
                            .jobCompany(companyList.stream().filter(company -> company.getId().equals(job.getCompany().getId())).findFirst().orElseThrow().getCompanyName())
                            .jobArea(areaList.stream().filter(area -> area.getId().equals(job.getArea().getId())).findFirst().orElseThrow().getAreaName())
                            .jobLocation(job.getJobLocation())
                            .jobSalary(job.getJobSalary())
                            .jobContent(job.getJobContent())
                            .jobUrl(job.getJobUrl())
                            .build()).collect(Collectors.toList());
            return new PageImpl<>(jobInfoList, pageable, jobByJobNameLike.getTotalElements());
        }
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    /**
     * @param jobId
     * @return
     */
    @Override
    @Transactional
    public JobInfo reload(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found, id:" + jobId));
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobName(job.getJobName());
        jobInfo.setJobCompany(job.getCompany().getCompanyName());
        jobInfo.setJobArea(job.getArea().getAreaName());
        jobInfo.setJobUrl(job.getJobUrl());
        return processJobInfoService.getJobInfo(job, jobInfo, job.getJobUrl(), new ChromeDriver(new ChromeOptions().setHeadless(false)), true);
    }

    /**
     * @return
     */
    @Override
    public List<JobArea> getJobAreaList() {
        QArea condition = QArea.area;
        Iterable<Area> iterable = areaRepository.findAll(condition.status.ne(JobStatus.DELETE));
        List<Area> result = new ArrayList<>();
        iterable.forEach(result::add);
        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptyList();
        }
        return result.stream().map(area -> new JobArea(area.getId(), area.getAreaName())).collect(Collectors.toList());
    }
}
