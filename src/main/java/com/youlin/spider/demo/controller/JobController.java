package com.youlin.spider.demo.controller;

import com.youlin.spider.demo.service.JobService;
import com.youlin.spider.demo.vo.JobArea;
import com.youlin.spider.demo.vo.JobInfo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    @GetMapping("/")
    @ApiOperation("取得Job列表")
    public ResponseEntity<Page<JobInfo>> getJobs(@RequestParam(required = false) String jobName,
                                                 @RequestParam(required = false) Integer jobAreaId,
                                                 @RequestParam(required = false) String jobCompanyName,
                                                 @RequestParam(required = false) String jobContent,
                                                 @PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(jobService.getJobs(jobName, jobAreaId, jobCompanyName, jobContent, pageable));
    }

    @GetMapping("/area/list")
    @ApiOperation("取得JobArea列表")
    public ResponseEntity<List<JobArea>> getJobAreaList() {
        return ResponseEntity.ok().body(jobService.getJobAreaList());
    }

    @PostMapping("/{jobId}/reload")
    @ApiOperation("重新取得Job內容")
    public ResponseEntity<JobInfo> updateJob(@PathVariable Integer jobId) {
        return ResponseEntity.ok(jobService.reload(jobId));
    }

    @PostMapping("/{jobId}/read")
    @ApiOperation("設定Job已讀")
    public ResponseEntity<Void> readJob(@PathVariable Integer jobId) {
        jobService.readJob(jobId);
        return ResponseEntity.ok().build();
    }
}
