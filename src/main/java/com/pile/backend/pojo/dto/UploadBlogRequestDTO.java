package com.pile.backend.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadBlogRequestDTO {
    @ApiModelProperty(value = "作者")
    private String username;

    @ApiModelProperty(value = "contenue")
    private String contenue;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "city")
    private String city;

    @ApiModelProperty(value = "image")
    private String image;
}
