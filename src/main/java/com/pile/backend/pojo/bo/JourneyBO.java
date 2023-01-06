package com.pile.backend.pojo.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class JourneyBO {
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

    @ApiModelProperty(value = "营业模式")
    private String commercialMode;

    @ApiModelProperty(value = "路程时长(分钟)")
    private int duration;

    @ApiModelProperty(value = "价格")
    private double prix;
}
