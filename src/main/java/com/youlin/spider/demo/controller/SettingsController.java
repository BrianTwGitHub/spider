package com.youlin.spider.demo.controller;

import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import com.youlin.spider.demo.service.FilterService;
import com.youlin.spider.demo.valid.FilterValid;
import com.youlin.spider.demo.vo.FilterInfo;
import io.swagger.annotations.ApiOperation;
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
public class SettingsController {

    private final FilterService filterService;

    @PostMapping("/filter")
    @ApiOperation("新增filter")
    public ResponseEntity<Void> createFilter(@Validated(FilterValid.Create.class) @RequestBody FilterInfo filterInfo) {
        filterService.createFilter(filterInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    @ApiOperation("取得Filter列表")
    public ResponseEntity<Page<FilterInfo>> searchFilter(@RequestParam(required = false) String filterName,
                                                         @RequestParam(required = false) FilterType filterType,
                                                         @RequestParam(required = false) StatusType statusType,
                                                         @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(filterService.searchFilter(filterName, filterType, statusType, pageable));
    }

    @PatchMapping("/filter")
    @ApiOperation("更新filter")
    public ResponseEntity<Void> updateFilter(@RequestBody FilterInfo filterInfo) {
        filterService.updateFilter(filterInfo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/filter")
    @ApiOperation("刪除filter")
    public ResponseEntity<Void> deleteFilter(@RequestParam Integer filterId) {
        filterService.deleteFilter(filterId);
        return ResponseEntity.ok().build();
    }
}
