package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class VoitureModelListRequestDTO {
    @ApiModelProperty(value = "品牌")
    private String marque;

    @ApiModelProperty(value = "汽车型号")
    private String modele;
}
