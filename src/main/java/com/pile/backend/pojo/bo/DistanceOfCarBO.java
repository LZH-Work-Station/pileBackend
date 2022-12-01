package com.pile.backend.pojo.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistanceOfCarBO {
    @ApiModelProperty(value = "出发地点")
    private String source;

    @ApiModelProperty(value = "结束地点")
    private String destination;

    @ApiModelProperty(value = "距离(km)")
    private double distance;
}
