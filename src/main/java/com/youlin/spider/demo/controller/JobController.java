package com.youlin.spider.demo.controller;

import com.youlin.spider.demo.service.JobService;
import com.youlin.spider.demo.vo.JobInfo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    @GetMapping
    @ApiOperation("取得Job列表")
    public ResponseEntity<Page<JobInfo>> getJobs(@RequestParam(required = false) String jobName,
                                                 @RequestParam(required = false) Integer jobAreaId,
                                                 @RequestParam(required = false) String jobContent,
                                                 @PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(jobService.getJobs(jobName, jobAreaId, jobContent, pageable));
    }

    @PostMapping("/{jobId}/reload")
    @ApiOperation("重新取得Job內容")
    public ResponseEntity<JobInfo> updateJob(@PathVariable Integer jobId) {
        return ResponseEntity.ok(jobService.reload(jobId));
    }
}
