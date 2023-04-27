package com.youlin.spider.demo.model.sub;

import com.youlin.spider.demo.model.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: xavier
 * @createdate: 2023/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ListItem extends Item {
	private int position;
	private String item;
}
