package com.youlin.spider.demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "Asia/Taipei")
    private Date jobUpdateDate;
    private String jobCompanyUrl;
    private boolean isRead;
    private boolean isFavorite;
}