package com.youlin.spider.demo.model.sub;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * @author: xavier
 * @createdate: 2023/4/26
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "PostalAddress")
public class Address {
	private String streetAddress;
	private String addressLocality;
	private String postalCode;
	private String addressRegion;
	private String addressCountry;
}
