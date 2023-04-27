package com.youlin.spider.demo.model;

import com.youlin.spider.demo.model.sub.ListItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: xavier
 * @createdate: 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BreadcrumbList extends BaseElement {
	private List<ListItem> itemListElement;
}
