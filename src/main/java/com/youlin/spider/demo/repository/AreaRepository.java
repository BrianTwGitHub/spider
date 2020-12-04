package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Integer>, QuerydslPredicateExecutor<Area> {
    Optional<Area> findAreaByAreaName(String areaName);
}