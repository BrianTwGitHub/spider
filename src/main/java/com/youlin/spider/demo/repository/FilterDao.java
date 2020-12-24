package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Filter;
import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterDao {

    Page<Filter> findFilterByCondition(String filterName, FilterType filterType, StatusType statusType, Pageable pageable);
}
