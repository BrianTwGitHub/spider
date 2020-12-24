package com.youlin.spider.demo.service;

import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import com.youlin.spider.demo.vo.FilterInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterService {
    /**
     * 建立 filter
     *
     * @param filterInfo
     */
    void createFilter(FilterInfo filterInfo);

    /**
     * 查詢 filter
     *
     * @param filterName
     * @param filterType
     * @param statusType
     * @param pageable
     * @return
     */
    Page<FilterInfo> searchFilter(String filterName, FilterType filterType, StatusType statusType, Pageable pageable);

    /**
     * 更新 filter
     *
     * @param filterInfo
     */
    void updateFilter(FilterInfo filterInfo);

    /**
     * 冊除 filter
     *
     * @param filterId
     */
    void deleteFilter(Integer filterId);
}
