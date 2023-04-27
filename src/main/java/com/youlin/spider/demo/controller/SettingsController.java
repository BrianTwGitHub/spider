package com.youlin.spider.demo.controller;

import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import com.youlin.spider.demo.service.FilterService;
import com.youlin.spider.demo.valid.FilterValid;
import com.youlin.spider.demo.vo.FilterInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
@Tag(name = "settings", description = "設定")
public class SettingsController {

	private final FilterService filterService;

	@PostMapping("/filter")
	@Operation(summary = "新增filter", description = "新增filter", tags = { "settings" })
	public ResponseEntity<Void> createFilter(@Validated(FilterValid.Create.class) @RequestBody FilterInfo filterInfo) {
		filterService.createFilter(filterInfo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/filter")
	@Operation(summary = "取得Filter列表", description = "取得Filter列表", tags = { "settings" })
	public ResponseEntity<Page<FilterInfo>> searchFilter(@RequestParam(required = false) String filterName,
		@RequestParam(required = false) FilterType filterType,
		@RequestParam(required = false) StatusType statusType,
		@PageableDefault Pageable pageable) {
		return ResponseEntity.ok(filterService.searchFilter(filterName, filterType, statusType, pageable));
	}

	@PatchMapping("/filter")
	@Operation(summary = "更新filter", description = "更新filter", tags = { "settings" })
	public ResponseEntity<Void> updateFilter(@RequestBody FilterInfo filterInfo) {
		filterService.updateFilter(filterInfo);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/filter")
	@Operation(summary = "刪除filter", description = "刪除filter", tags = { "settings" })
	public ResponseEntity<Void> deleteFilter(@RequestParam Integer filterId) {
		filterService.deleteFilter(filterId);
		return ResponseEntity.ok().build();
	}
}
