package com.youlin.spider.demo.batch;

import com.youlin.spider.demo.service.ProcessJobInfoService;
import com.youlin.spider.demo.vo.JobInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteJob {
    private final ProcessJobInfoService processJobInfoService;

    @Scheduled(cron = "0 12 12 * * *")
    public void executeGetJobs() {
        log.info("run executeGetJobs");

        List<JobInfo> jobInfos = processJobInfoService.processJobs(3, 1);
        log.info("total get jobs: {}", jobInfos.size());
    }
}
