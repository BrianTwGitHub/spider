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
                                                 @RequestParam(required = false) List<Integer> jobAreaIds,
                                                 @RequestParam(required = false) String jobCompanyName,
                                                 @RequestParam(required = false) String jobContent,
                                                 @RequestParam(required = false) Boolean isRead,
                                                 @RequestParam(required = false) Boolean isFavorite,
                                                 @PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(jobService.getJobs(jobName, jobAreaIds, jobCompanyName, jobContent, isRead, isFavorite, pageable));
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

    @PostMapping("/failed/reload")
    @ApiOperation("重新取得Job內容")
    public ResponseEntity<List<JobInfo>> failedReload() {
        return ResponseEntity.ok(jobService.failedReload());
    }

    @PostMapping("/{jobId}/read")
    @ApiOperation("設定Job已讀")
    public ResponseEntity<Void> readJob(@PathVariable Integer jobId) {
        jobService.readJob(jobId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{jobId}/favorite")
    @ApiOperation("加入/取消我的最愛")
    public ResponseEntity<Boolean> addFavorite(@PathVariable Integer jobId) {
        return ResponseEntity.ok().body(jobService.favoriteJob(jobId));
    }
}
