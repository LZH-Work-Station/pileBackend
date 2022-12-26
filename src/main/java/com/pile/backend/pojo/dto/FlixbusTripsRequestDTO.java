package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlixbusTripsRequestDTO {
    @ApiModelProperty(value = "起点")
    private String source;

    @ApiModelProperty(value = "终点")
    private String destination;

    @ApiModelProperty(value = "日期")
    private String date;
}
