package com.youlin.spider.demo.controller;

import com.youlin.spider.demo.service.ProcessJobInfoService;
import com.youlin.spider.demo.vo.JobInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trigger")
@RequiredArgsConstructor
public class TriggerController {
    private final ProcessJobInfoService processJobInfoService;

    @GetMapping("/jobs")
    public ResponseEntity<List<JobInfo>> processJobInfoList() {
        return ResponseEntity.ok(processJobInfoService.processJobs());
    }
}
