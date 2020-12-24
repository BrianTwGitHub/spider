package com.youlin.spider.demo.service.impl;

import com.youlin.spider.demo.entity.*;
import com.youlin.spider.demo.enums.StatusType;
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
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<JobInfo> getJobs(String jobName, List<Integer> jobAreaIds, String companyName, String jobContent, Boolean isRead, Boolean isFavorite, Pageable pageable) {
        Page<Job> jobByJobNameLike = jobDao.findJobByCondition(jobName, jobAreaIds, companyName, jobContent, null, isRead, isFavorite, pageable);
        if (!jobByJobNameLike.isEmpty()) {
            List<Area> areaList = areaRepository.findAll();
            List<Company> companyList = companyRepository.findAll();
            List<JobInfo> jobInfoList = jobByJobNameLike.getContent().stream()
                    .map(job -> {
                        Company thisCompany = companyList.stream().filter(company -> company.getId().equals(job.getCompany().getId())).findFirst().orElseThrow(() -> new IllegalArgumentException("can not found company, id:" + job.getCompany().getId()));
                        return JobInfo.builder()
                                .jobId(job.getId())
                                .jobName(job.getJobName())
                                .jobCompany(thisCompany.getCompanyName())
                                .jobArea(areaList.stream().filter(area -> area.getId().equals(job.getArea().getId())).findFirst().orElseThrow(() -> new IllegalArgumentException("can not found area, id: " + job.getArea().getId())).getAreaName())
                                .jobLocation(job.getJobLocation())
                                .jobSalary(job.getJobSalary())
                                .jobContent(job.getJobContent())
                                .jobUrl(job.getJobUrl())
                                .jobCompanyUrl(thisCompany.getCompanyUrl())
                                .isRead(job.isRead())
                                .isFavorite(job.isFavorite())
                                .jobUpdateDate(job.getJobUpdateDate())
                                .build();
                    }).collect(Collectors.toList());
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
        ChromeDriver chromeDriver = new ChromeDriver(new ChromeOptions().setHeadless(true).addArguments("--no-sandbox"));
        try {
            Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found, id:" + jobId));
            JobInfo jobInfo = new JobInfo();
            jobInfo.setJobName(job.getJobName());
            jobInfo.setJobCompany(job.getCompany().getCompanyName());
            jobInfo.setJobArea(job.getArea().getAreaName());
            jobInfo.setJobUrl(job.getJobUrl());
            return processJobInfoService.getJobInfo(job, jobInfo, job.getJobUrl(), chromeDriver, true);
        } finally {
            chromeDriver.quit();
        }
    }

    /**
     * 重新取得工作內容
     *
     * @return
     */
    @Override
    @Transactional
    public List<JobInfo> failedReload() {
        ChromeDriver chromeDriver = null;
        try {
            QJob qJob = QJob.job;
            Iterable<Job> iterable = jobRepository.findAll(qJob.status.ne(StatusType.DELETED).and(qJob.jobContent.eq("").or(qJob.jobUpdateDate.isNull())));
            if (!iterable.iterator().hasNext()) {
                return Collections.emptyList();

            }
            chromeDriver = new ChromeDriver(new ChromeOptions().setHeadless(true).addArguments("--no-sandbox"));

            List<JobInfo> jobInfoList = new ArrayList<>();
            for (Job job : iterable) {
                JobInfo jobInfo = new JobInfo();
                jobInfo.setJobName(job.getJobName());
                jobInfo.setJobCompany(job.getCompany().getCompanyName());
                jobInfo.setJobArea(job.getArea().getAreaName());
                jobInfo.setJobUrl(job.getJobUrl());
                processJobInfoService.getJobInfo(job, jobInfo, job.getJobUrl(), chromeDriver, true);
                jobInfoList.add(jobInfo);
            }
            return jobInfoList;
        } finally {
            if (chromeDriver != null) {
                chromeDriver.quit();
            }
        }
    }

    /**
     * @return
     */
    @Override
    public List<JobArea> getJobAreaList() {
        QArea condition = QArea.area;
        Iterable<Area> iterable = areaRepository.findAll(condition.status.ne(StatusType.DELETED));
        if (!iterable.iterator().hasNext()) {
            return Collections.emptyList();

        }
        List<Area> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result.stream().map(area -> new JobArea(area.getId(), area.getAreaName())).collect(Collectors.toList());
    }

    /**
     * 設定Job已讀
     *
     * @param jobId
     */
    @Override
    @Transactional
    public void readJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found, id:" + jobId));
        job.setRead(true);
        jobRepository.save(job);
    }

    /**
     * 加入/取消我的最愛
     *
     * @param jobId
     */
    @Override
    @Transactional
    public boolean favoriteJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found, id:" + jobId));
        boolean isFavorite = !job.isFavorite();
        job.setFavorite(isFavorite);
        jobRepository.save(job);
        return isFavorite;
    }

    /**
     * 重新取得公司連結
     */
    @Override
    @Transactional
    public void reloadCompanyUrl() {
        ChromeDriver chromeDriver = null;
        try {
            QCompany qCompany = QCompany.company;
            Iterable<Company> companies = companyRepository.findAll(qCompany.companyUrl.isNull());
            if (companies.iterator().hasNext()) {
                chromeDriver = new ChromeDriver(new ChromeOptions().setHeadless(true).addArguments("--no-sandbox"));
                for (Company company : companies) {
                    Job job = company.getJobs().get(0);
                    if (StatusType.DELETED != job.getStatus()) {
                        String jobUrl = company.getJobs().get(0).getJobUrl();
                        chromeDriver.get(jobUrl);
                        try {
                            String companyUrl = chromeDriver.findElementByClassName("job-header__title").findElement(By.tagName("a")).getAttribute("href");
                            company.setCompanyUrl(companyUrl);
                            companyRepository.save(company);
                        } catch (Exception e) {
                            log.error("can't find company url: {}", company.getCompanyName(), e);
                        }
                    }
                }
            }
        } finally {
            if (chromeDriver != null) {
                chromeDriver.quit();
            }
        }

    }

}
