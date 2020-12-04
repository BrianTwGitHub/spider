package com.youlin.spider.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInfo {
    private Integer jobId;
    private String jobName;
    private String jobCompany;
    private String jobContent;
    private String jobArea;
    private String jobSalary;
    private String jobLocation;
    private String jobUrl;
}