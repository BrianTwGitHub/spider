package com.youlin.spider.demo.service.impl;

import com.youlin.spider.demo.entity.Filter;
import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import com.youlin.spider.demo.repository.FilterDao;
import com.youlin.spider.demo.repository.FilterRepository;
import com.youlin.spider.demo.service.FilterService;
import com.youlin.spider.demo.vo.FilterInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final FilterRepository filterRepository;

    private final FilterDao filterDao;

    /**
     * 建立 filter
     *
     * @param filterInfo
     */
    @Override
    public void createFilter(FilterInfo filterInfo) {
        Optional<Filter> optional = filterRepository.findFilterByFilterNameAndFilterType(filterInfo.getFilterName(), filterInfo.getFilterType());
        Filter filter;
        if (optional.isPresent()) {
            filter = optional.get();
            if (StatusType.DISABLED == filter.getStatus()) {
                filter.setStatus(StatusType.ACTIVATED);
                filterRepository.save(filter);
            }
        } else {
            filter = new Filter();
            filter.setFilterName(filterInfo.getFilterName());
            filter.setFilterType(filterInfo.getFilterType());
            filter.setStatus(StatusType.ACTIVATED);
            filterRepository.save(filter);
        }
    }

    /**
     * 查詢 filter
     *
     * @param filterName
     * @param filterType
     * @param statusType
     * @param pageable
     * @return
     */
    @Override
    public Page<FilterInfo> searchFilter(String filterName, FilterType filterType, StatusType statusType, Pageable pageable) {
        Page<Filter> filterByCondition = filterDao.findFilterByCondition(filterName, filterType, statusType, pageable);
        if (!filterByCondition.isEmpty()) {
            List<FilterInfo> filterInfoList = filterByCondition.getContent().stream()
                    .map(filter ->
                            FilterInfo.builder()
                                    .id(filter.getId())
                                    .filterName(filter.getFilterName())
                                    .filterType(filter.getFilterType())
                                    .status(filter.getStatus())
                                    .build()
                    ).collect(Collectors.toList());
            return new PageImpl<>(filterInfoList, pageable, filterByCondition.getTotalElements());
        }
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    /**
     * 更新 filter
     *
     * @param filterInfo
     */
    @Override
    public void updateFilter(FilterInfo filterInfo) {
        Optional<Filter> optional = filterRepository.findFilterByFilterNameAndFilterType(filterInfo.getFilterName(), filterInfo.getFilterType());
        if (optional.isPresent() && !optional.get().getId().equals(filterInfo.getId())) {
            throw new IllegalArgumentException(String.format("過濾條件已存在 filterName: %s, filterType: %s", filterInfo.getFilterName(), filterInfo.getFilterType()));
        }
        Filter orgFilter = filterRepository.findById(filterInfo.getId()).orElseThrow(IllegalArgumentException::new);
        orgFilter.setFilterName(filterInfo.getFilterName());
        orgFilter.setFilterType(filterInfo.getFilterType());
        orgFilter.setStatus(filterInfo.getStatus());
        filterRepository.save(orgFilter);
    }

    /**
     * 冊除 filter
     *
     * @param filterId
     */
    @Override
    public void deleteFilter(Integer filterId) {
        Optional<Filter> byId = filterRepository.findById(filterId);
        if (byId.isPresent()) {
            Filter filter = byId.get();
            filter.setStatus(StatusType.DELETED);
            filterRepository.save(filter);
        }
    }
}
