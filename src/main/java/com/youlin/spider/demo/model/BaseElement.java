package com.youlin.spider.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author: xavier
 * @createdate: 2023/4/26
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@JsonSubTypes({
	@JsonSubTypes.Type(value = BreadcrumbList.class, name = "BreadcrumbList"),
	@JsonSubTypes.Type(value = WebPage.class, name = "WebPage"),
	@JsonSubTypes.Type(value = JobPosting.class, name = "JobPosting") })
@Data
public abstract class BaseElement {
	@JsonProperty("@context")
	private String context;
}
