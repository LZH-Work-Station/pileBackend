package com.pile.backend.controller;

import com.pile.backend.common.result.Result;
import com.pile.backend.pojo.dto.JourneyRequestDTO;
import com.pile.backend.pojo.dto.VoitureCo2RequestDTO;
import com.pile.backend.pojo.dto.VoitureMarqueListRequestDTO;
import com.pile.backend.pojo.dto.VoitureModelListRequestDTO;
import com.pile.backend.pojo.vo.JourneyListVO;
import com.pile.backend.pojo.vo.VoitureCo2AndDistVO;
import com.pile.backend.pojo.vo.VoitureMarqueListVO;
import com.pile.backend.pojo.vo.VoitureModelListVO;
import com.pile.backend.service.VoitureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/voiture")
@CrossOrigin
public class VoitureController {

    private static final Logger logger = LogManager.getLogger(VoitureController.class);

    @Autowired
    private VoitureService voitureService;

    @PostMapping("getVoitureCo2")
    @ApiOperation(value = "获得汽车两地距离和co2")
    public Result getVoitureCo2Info(@RequestBody VoitureCo2RequestDTO voitureCo2RequestDTO){
            logger.info("Request the Voiture Info : " + voitureCo2RequestDTO.toString());
            VoitureCo2AndDistVO voitureCo2AndDist = voitureService.getVoitureCo2AndDist(voitureCo2RequestDTO);
            return Result.ok(voitureCo2AndDist);
    }


    @PostMapping("getVoitureMarqueList")
    @ApiOperation(value = "获得汽车列表")
    public Result getJourneyInfo(@RequestBody VoitureMarqueListRequestDTO voitureMarqueListRequestDTO){
        logger.info("Request the Voiture marque Info : " + voitureMarqueListRequestDTO.toString());
        VoitureMarqueListVO voitureMarqueListVO = voitureService.getVoitureMarqueList(voitureMarqueListRequestDTO);
        return Result.ok(voitureMarqueListVO);
    }

    @PostMapping("getVoitureModelList")
    @ApiOperation(value = "获得汽车列表")
    public Result getJourneyInfo(@RequestBody VoitureModelListRequestDTO voitureModelListRequestDTO){
        logger.info("Request the Voiture marque Info : " + voitureModelListRequestDTO.toString());
        VoitureModelListVO voitureMarqueListVO = voitureService.getVoitureModelList(voitureModelListRequestDTO);
        return Result.ok(voitureMarqueListVO);
    }
}
