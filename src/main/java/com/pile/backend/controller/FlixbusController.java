package com.pile.backend.controller;

import com.pile.backend.common.result.Result;
import com.pile.backend.pojo.dto.CityStationsRequestDTO;
import com.pile.backend.pojo.dto.FlixbusTripsRequestDTO;
import com.pile.backend.pojo.vo.CityStationsVO;
import com.pile.backend.pojo.vo.FlixbusTripsVO;
import com.pile.backend.service.FlixbusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/flixbus")
@CrossOrigin
public class FlixbusController {
    @Autowired
    private FlixbusService flixbusService;
    private static final Logger logger = LogManager.getLogger(FlixbusController.class);

    @PostMapping("getCityStations")
    @ApiOperation(value = "获得城市的车站")
    public Result getCityStations(@RequestBody CityStationsRequestDTO cityStationsRequestDTO){
        try{
            logger.info("Request the JourneyInfo : " + cityStationsRequestDTO.toString());
            CityStationsVO cityStationsVO = flixbusService.getCityStations(cityStationsRequestDTO);
            return Result.ok(cityStationsVO);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("getFlixbusTrips")
    @ApiOperation(value = "获得路线")
    public Result getFlixbusTrips(@RequestBody FlixbusTripsRequestDTO flixbusTripsRequestDTO){
        try{
            logger.info("Request the FlixbusTrips : " + flixbusTripsRequestDTO.toString());
            FlixbusTripsVO flixbusTrips = flixbusService.getFlixbusTrips(flixbusTripsRequestDTO);
            return Result.ok(flixbusTrips);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }
}
