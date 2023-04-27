package com.youlin.spider.demo.service;

import com.youlin.spider.demo.entity.Job;
import com.youlin.spider.demo.vo.JobInfo;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public interface ProcessJobInfoService {
	List<JobInfo> processJobs(Integer effectiveDays, Integer userId);

	JobInfo getJobInfo(Job job, JobInfo jobInfo, ChromeDriver headerChromeDriver, boolean oldJob);
}
