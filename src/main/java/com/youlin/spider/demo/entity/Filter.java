package com.youlin.spider.demo.entity;

import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Table(name = "filter")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Filter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "filter_name", unique = true)
	private String filterName;

	@Enumerated(EnumType.STRING)
	@Column(name = "filter_type", nullable = false)
	private FilterType filterType;

	@Column(name = "user_id")
	private Integer userId;

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
