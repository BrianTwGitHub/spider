package com.youlin.spider.demo.entity;

import com.youlin.spider.demo.enums.StatusType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "area")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "area_name", unique = true)
	private String areaName;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StatusType status;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false)
	private Date createDate;

	@OneToMany(mappedBy = "area")
	private List<Job> jobs = new ArrayList<>();
}
