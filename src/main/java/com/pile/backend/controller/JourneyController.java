package com.pile.backend.controller;
import com.pile.backend.common.result.Result;
import com.pile.backend.pojo.dto.CityJourneyRequestDTO;
import com.pile.backend.pojo.dto.GareRequestDTO;
import com.pile.backend.pojo.dto.JourneyRequestDTO;
import com.pile.backend.pojo.vo.CityJourneyListVO;
import com.pile.backend.pojo.vo.GareListVO;
import com.pile.backend.pojo.vo.JourneyListVO;
import com.pile.backend.service.JourneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/journey")
@CrossOrigin
public class JourneyController {
    @Autowired
    private JourneyService journeyService;
    private static final Logger logger = LogManager.getLogger(JourneyController.class);

    @PostMapping("getJourneyInfo")
    @ApiOperation(value = "获得查询的票的信息")
    public Result getJourneyInfo(@RequestBody JourneyRequestDTO journeyRequestDTO){
        try{
            logger.info("Request the JourneyInfo : " + journeyRequestDTO.toString());
            JourneyListVO journeyListVO = journeyService.getJourneyInfo(journeyRequestDTO);
            return Result.ok(journeyListVO);
        }catch (Exception e){
            logger.error(e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("getCityGare")
    @ApiOperation(value = "获得城市的所有火车站")
    public Result getCityGare(@RequestBody GareRequestDTO gareRequestDTO){
        try{
            logger.info("Request the JourneyInfo : " + gareRequestDTO.toString());
            GareListVO gareListVO = journeyService.getGareInfo(gareRequestDTO);
            return Result.ok(gareListVO);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("getJourneyInfoOfCity")
    @ApiOperation(value = "获得城市的票的信息")
    public Result getJourneyInfo(@RequestBody CityJourneyRequestDTO cityJourneyRequestDTO) {
        try {
            logger.info("Request the JourneyInfo : " + cityJourneyRequestDTO.toString());
            CityJourneyListVO cityJourneyListVO = journeyService.getCityJourneyListInfo(cityJourneyRequestDTO);
            return Result.ok(cityJourneyListVO);
        } catch (Exception e) {
            logger.error(e);
            return Result.fail(e.getMessage());
        }
    }
}
