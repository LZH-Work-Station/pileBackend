package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityJourneyRequestDTO {
    @ApiModelProperty(value = "出发日期")
    private String date;

    @ApiModelProperty(value = "小时")
    @Builder.Default
    private int hour = -1;

    @ApiModelProperty(value = "起点")
    private String sourceCity;

    @ApiModelProperty(value = "终点")
    private String destinationCity;
}
