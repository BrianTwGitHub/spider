package com.youlin.spider.demo.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.youlin.spider.demo.entity.Filter;
import com.youlin.spider.demo.entity.QFilter;
import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import com.youlin.spider.demo.repository.FilterDao;
import com.youlin.spider.demo.repository.FilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilterDaoImpl implements FilterDao {

    private final FilterRepository filterRepository;

    @Override
    public Page<Filter> findFilterByCondition(String filterName, FilterType filterType, StatusType statusType, Pageable pageable) {
        QFilter qFilter = QFilter.filter;

        BooleanExpression conditions = Expressions.asBoolean(true).isTrue();

        if (StringUtils.hasLength(filterName)) {
            conditions = conditions.and(qFilter.filterName.equalsIgnoreCase(filterName));
        }

        if (filterType != null) {
            conditions = conditions.and(qFilter.filterType.eq(filterType));
        }

        if (statusType != null) {
            conditions = conditions.and(qFilter.status.eq(statusType));
        }

        conditions = conditions.and(qFilter.status.ne(StatusType.DELETED));

        return filterRepository.findAll(conditions, pageable);

    }
}
