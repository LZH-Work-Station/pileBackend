package com.pile.backend.pojo.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TicketRequestDTO {
    @ApiModelProperty(value = "出发日期")
    private String date;

    @ApiModelProperty(value = "起点")
    private String source;

    @ApiModelProperty(value = "终点")
    private String destination;
}
