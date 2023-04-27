package com.youlin.spider.demo.model;

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
public class WebPage extends BaseElement {
	private String name;
	private String url;
	private String relatedLink;
	private Organization publisher;
	private List<String> significantLink;
}
