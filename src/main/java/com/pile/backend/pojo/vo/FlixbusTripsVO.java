package com.pile.backend.pojo.vo;

import com.pile.backend.pojo.bo.FlixbusTripBO;
import lombok.Data;

import java.util.List;

@Data
public class FlixbusTripsVO {
    private String source;
    private String sourceAddress;

    private String destination;
    private String destinationAddress;

    private List<FlixbusTripBO> flixbusTripBOs;
}
