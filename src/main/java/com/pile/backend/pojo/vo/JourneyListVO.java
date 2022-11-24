package com.pile.backend.pojo.vo;

import com.pile.backend.pojo.bo.JourneyBO;
import lombok.Data;

import java.util.List;

@Data
public class JourneyListVO {
    private List<JourneyBO> journeyVOList;
}
