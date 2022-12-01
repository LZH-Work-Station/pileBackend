package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoitureCo2RequestDTO {
    @ApiModelProperty(value = "起点")
    private String source;

    @ApiModelProperty(value = "终点")
    private String destination;

    @ApiModelProperty(value = "汽车牌子")
    private String marque;

    @ApiModelProperty(value = "汽车牌子")
    private String modele;
}
