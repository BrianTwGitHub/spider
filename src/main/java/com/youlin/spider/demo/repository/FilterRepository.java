package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Filter;
import com.youlin.spider.demo.enums.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Integer>, QuerydslPredicateExecutor<Filter> {
	Optional<Filter> findFilterByFilterNameAndFilterType(String filterName, FilterType filterType);

}