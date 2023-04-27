package com.youlin.spider.demo.controller;

import com.youlin.spider.demo.service.JobService;
import com.youlin.spider.demo.vo.JobArea;
import com.youlin.spider.demo.vo.JobInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
@Tag(name = "job", description = "工作")
@Slf4j
public class JobController {

	private final JobService jobService;
	
	@GetMapping("/")
	@Operation(summary = "取得Job列表", description = "取得Job列表", tags = { "job" })
	public ResponseEntity<Page<JobInfo>> getJobs(@RequestParam(defaultValue = "1") Integer userId,
		@RequestParam(required = false) String jobName,
		@RequestParam(required = false) List<Integer> jobAreaIds,
		@RequestParam(required = false) String jobCompanyName,
		@RequestParam(required = false) String jobContent,
		@RequestParam(required = false) Boolean isRead,
		@RequestParam(required = false) Boolean isFavorite,
		@PageableDefault Pageable pageable) {
		return ResponseEntity.ok().body(jobService.getJobs(userId, jobName, jobAreaIds, jobCompanyName, jobContent, isRead, isFavorite, pageable));
	}

	@GetMapping("/area/list")
	@Operation(summary = "取得JobArea列表", description = "取得JobArea列表", tags = { "job" })

	public ResponseEntity<List<JobArea>> getJobAreaList() {
		return ResponseEntity.ok().body(jobService.getJobAreaList());
	}

	@PostMapping("/{jobId}/reload")
	@Operation(summary = "重新取得Job內容", description = "重新取得Job內容", tags = { "job" })

	public ResponseEntity<JobInfo> updateJob(@PathVariable Integer jobId) {
		return ResponseEntity.ok(jobService.reload(jobId));
	}

	@PostMapping("/failed/reload")
	@Operation(summary = "重新取得Job內容", description = "重新取得Job內容", tags = { "job" })

	public ResponseEntity<List<JobInfo>> failedReload() {
		return ResponseEntity.ok(jobService.failedReload());
	}

	@PostMapping("/failed/company/reload")
	@Operation(summary = "重新取得Company url內容", description = "重新取得Company url內容", tags = { "job" })

	public ResponseEntity<Void> failedCompanyReload() {
		jobService.reloadCompanyUrl();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{jobId}/read")
	@Operation(summary = "設定Job已讀", description = "設定Job已讀", tags = { "job" })
	public ResponseEntity<Void> readJob(@PathVariable Integer jobId) {
		jobService.readJob(jobId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{jobId}/favorite")
	@Operation(summary = "加入/取消我的最愛", description = "加入/取消我的最愛", tags = { "job" })
	public ResponseEntity<Boolean> addFavorite(@PathVariable Integer jobId) {
		return ResponseEntity.ok().body(jobService.favoriteJob(jobId));
	}
}
