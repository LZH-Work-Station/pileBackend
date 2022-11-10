package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Journey {
    @ApiModelProperty(value = "出发地点")
    private String source;

    @ApiModelProperty(value = "目的地点")
    private String destination;

    @ApiModelProperty(value = "出发时间")
    private String departureTime;

    @ApiModelProperty(value = "到达时间")
    private String arrivalTime;

    @ApiModelProperty(value = "Co2排放量")
    private double co2Emission;

    @ApiModelProperty(value = "路程时长(分钟)")
    private int duration;
}
