package com.pile.backend.pojo.bo;

import lombok.Data;

@Data
public class FlixbusTripBO {
    private String depatureTime;
    private String arrivalTime;
    private String duration;
    private String price;
    private double co2Emission;
    private String msg;
}
