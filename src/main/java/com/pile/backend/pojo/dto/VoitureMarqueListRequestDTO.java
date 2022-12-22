package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class VoitureMarqueListRequestDTO {
    @ApiModelProperty(value = "汽车牌子")
    private String marque;

    public VoitureMarqueListRequestDTO() {}
}
