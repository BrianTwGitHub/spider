package com.youlin.spider.demo.vo;

import com.youlin.spider.demo.enums.FilterType;
import com.youlin.spider.demo.enums.StatusType;
import com.youlin.spider.demo.valid.FilterValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterInfo {

    @NotNull(groups = {FilterValid.Update.class})
    private Integer id;

    @NotNull(groups = {FilterValid.Create.class, FilterValid.Update.class})
    private String filterName;

    @NotNull(groups = {FilterValid.Create.class, FilterValid.Update.class})
    private FilterType filterType;

    private StatusType status;
}
