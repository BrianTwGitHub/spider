package com.youlin.spider.demo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.youlin.spider.demo.repository")
@EntityScan("com.youlin.spider.demo.entity")
@EnableJpaAuditing
public class JpaConfiguration {
}
