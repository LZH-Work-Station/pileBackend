package com.pile.backend.pojo.vo;

import com.pile.backend.pojo.bo.JourneyBO;
import lombok.Data;

import java.util.List;


@Data
public class VoitureCo2AndDistVO {
    private String source;
    private String destination;
    private double distance;
    private double co2Emission;
}
