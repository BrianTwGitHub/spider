package com.youlin.spider.demo.entity;

import com.youlin.spider.demo.enums.StatusType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "job")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "job_name", nullable = false)
	private String jobName;

	@Column(name = "job_content")
	private String jobContent;

	@Column(name = "job_salary")
	private String jobSalary;

	@Column(name = "job_location")
	private String jobLocation;

	@Column(name = "job_url")
	private String jobUrl;

	@Column(name = "is_read")
	private boolean isRead;

	@Column(name = "is_favorite")
	private boolean isFavorite;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_company_id")
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_area_id", nullable = false)
	private Area area;

	@Column(name = "user_id")
	private Integer userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "job_update_date")
	private Date jobUpdateDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StatusType status;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false)
	private Date createDate;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

}
