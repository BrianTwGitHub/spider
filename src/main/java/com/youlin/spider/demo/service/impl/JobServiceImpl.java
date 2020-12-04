package com.youlin.spider.demo.service.impl;

import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.enums.JobStatus;
import com.youlin.spider.demo.repository.JobDao;
import com.youlin.spider.demo.repository.JobRepository;
import com.youlin.spider.demo.service.JobService;
import com.youlin.spider.demo.service.ProcessJobInfoService;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobDao jobDao;

    private final JobRepository jobRepository;

    private final ProcessJobInfoService processJobInfoService;

    @Override
    public Page<JobInfo> getJobs(String jobName, Integer jobAreaId, String jobContent, Pageable pageable) {
        Page<Job> jobByJobNameLike = jobDao.findJobByCondition(jobName, jobAreaId, jobContent, JobStatus.DELETE, pageable);
        List<JobInfo> jobInfoList = jobByJobNameLike.getContent().stream()
                .map(job -> JobInfo.builder()
                        .jobId(job.getId())
                        .jobName(job.getJobName())
                        .jobCompany(job.getCompany().getCompanyName())
                        .jobArea(job.getArea().getAreaName())
                        .jobLocation(job.getJobLocation())
                        .jobSalary(job.getJobSalary())
                        .jobContent(job.getJobContent())
                        .build()).collect(Collectors.toList());
        return new PageImpl<>(jobInfoList, pageable, jobByJobNameLike.getTotalElements());
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
}
