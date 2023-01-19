package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class CityBlogDTO {
    @ApiModelProperty(value = "城市")
    private String city;
}
