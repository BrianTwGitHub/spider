package com.youlin.spider.demo.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.youlin.spider.demo.model.sub.Identifier;
import com.youlin.spider.demo.model.sub.ListItem;
import com.youlin.spider.demo.model.sub.Organization;
import lombok.Data;

/**
 * @author: xavier
 * @createdate: 2023/4/26
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@Data
@JsonSubTypes({
	@JsonSubTypes.Type(value = Organization.class, names = { "HiringOrganization", "Organization" }),
	@JsonSubTypes.Type(value = ListItem.class),
	@JsonSubTypes.Type(value = Identifier.class, name = "PropertyValue"),
})
public abstract class Item {
	private String name;
}
