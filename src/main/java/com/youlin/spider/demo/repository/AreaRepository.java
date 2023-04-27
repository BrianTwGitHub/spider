package com.youlin.spider.demo.repository;

import com.youlin.spider.demo.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer>, QuerydslPredicateExecutor<Area> {
	Optional<Area> findAreaByAreaName(String areaName);
}