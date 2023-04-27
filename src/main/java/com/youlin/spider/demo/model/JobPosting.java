package com.youlin.spider.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.youlin.spider.demo.model.sub.BaseSalary;
import com.youlin.spider.demo.model.sub.Identifier;
import com.youlin.spider.demo.model.sub.JobLocation;
import com.youlin.spider.demo.model.sub.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: xavier
 * @createdate: 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "mainEntityOfPage", "educationRequirements", "experienceRequirements" })
public class JobPosting extends BaseElement {
	private String title;
	private String url;
	private String datePosted;
	private String validThrough;
	private String description;
	private String employmentType;
	private Organization hiringOrganization;
	private Identifier identifier;
	private JobLocation jobLocation;
	private BaseSalary baseSalary;
	private String industry;
	private String workHours;
	private List<String> skills;
}
