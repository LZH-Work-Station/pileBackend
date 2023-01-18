package com.pile.backend.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pile.backend.common.util.DateTimeUtil;
import com.pile.backend.common.util.RestfulRequestUtil;
import com.pile.backend.pojo.bo.FlixbusTripBO;
import com.pile.backend.pojo.dto.CityStationsRequestDTO;
import com.pile.backend.pojo.dto.FlixbusTripsRequestDTO;
import com.pile.backend.pojo.po.FlixbusStation;
import com.pile.backend.pojo.po.mapper.FlixbusStationMapper;
import com.pile.backend.pojo.vo.CityStationsVO;
import com.pile.backend.pojo.vo.FlixbusTripsVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

@Service
public class FlixbusService {
    private static final Logger logger = LogManager.getLogger(FlixbusService.class);
    private static final String url = "https://flixbus.p.rapidapi.com/v1/search-trips?to_id={destination}&from_id={source}&currency=EUR&departure_date={date}&number_adult=1&search_by=stations";
    private static final String token = "25d27827demsh606a5a6643ae4f9p1c537ajsnf88fda967d9a";

    @Autowired
    private RestfulRequestUtil restfulRequestUtil;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Autowired
    private FlixbusStationMapper flixbusStationMapper;

    public CityStationsVO getCityStations(CityStationsRequestDTO cityStationsRequestDTO) {
        QueryWrapper<FlixbusStation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.eq("city_name", cityStationsRequestDTO.getCity());
        List<FlixbusStation> flixbusStations = flixbusStationMapper.selectList(sourceQueryWrapper);

        CityStationsVO cityStationsVO = new CityStationsVO();
        List<String> cityStations = new ArrayList<>();
        for(FlixbusStation flixbusStation: flixbusStations){
            cityStations.add(flixbusStation.getName());
        }
        cityStationsVO.setCityStations(cityStations);
        return cityStationsVO;
    }

    public FlixbusTripsVO getFlixbusTrips(FlixbusTripsRequestDTO flixbusTripsRequestDTO) {
        // 获得起始车站信息
        QueryWrapper<FlixbusStation> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.eq("name", flixbusTripsRequestDTO.getSource());
        FlixbusStation sourceStation = flixbusStationMapper.selectOne(sourceQueryWrapper);
        String source = sourceStation.getStationId();

        // 获得终点车站信息
        QueryWrapper<FlixbusStation> destinationQueryWrapper = new QueryWrapper<>();
        destinationQueryWrapper.eq("name", flixbusTripsRequestDTO.getDestination());
        FlixbusStation destinationStation = flixbusStationMapper.selectOne(destinationQueryWrapper);
        String destination = destinationStation.getStationId();

        // 获得url并request flixbus
        String date = flixbusTripsRequestDTO.getDate();
        logger.info("Request from source = " + source + " destination = " + destination + " for trips");
        String url = FlixbusService.url.replace("{destination}", destination).replace("{source}", source).replace("{date}", date);
        JSONArray flixbustrips = restfulRequestUtil.doFlixbusSearchGet(url);

        // 构造回复
        FlixbusTripsVO flixbusTripsVO = new FlixbusTripsVO();
        flixbusTripsVO.setSource(sourceStation.getName());
        flixbusTripsVO.setSourceAddress(sourceStation.getAddress());
        flixbusTripsVO.setDestination(destinationStation.getName());
        flixbusTripsVO.setDestinationAddress(destinationStation.getAddress());
        List<FlixbusTripBO> flixbusTripBOs = new ArrayList<>();

        JSONArray timetables = flixbustrips.getJSONObject(0).getJSONArray("items");

        for(int i=0;i<timetables.size();i++){
            JSONObject timetable = timetables.getJSONObject(i);
            Long departureTimestamp = timetable.getJSONObject("departure").getLong("timestamp");
            String departureTime = DateUtil.date(departureTimestamp*1000).toString();

            Long arrivalTimestamp = timetable.getJSONObject("arrival").getLong("timestamp");
            String arrivalTime = DateUtil.date(arrivalTimestamp*1000).toString();

            // 估计co2
            Long durationInSecond = arrivalTimestamp - departureTimestamp;
            double heure = (double)durationInSecond / 3600.0;
            double distance = heure * 65;
            double co2Emission = 0.11*1000*distance/50;

            Integer hour = timetable.getJSONObject("duration").getInt("hour");
            Integer minutes = timetable.getJSONObject("duration").getInt("minutes");
            String duration = hour + "h" + minutes;

            FlixbusTripBO flixbusTripBO = new FlixbusTripBO();
            flixbusTripBO.setCo2Emission(co2Emission);
            flixbusTripBO.setDepatureTime(departureTime);
            flixbusTripBO.setArrivalTime(arrivalTime);
            flixbusTripBO.setDuration(duration);
            flixbusTripBO.setPrice(timetable.getStr("price_total_sum"));
            flixbusTripBO.setMsg(timetable.getStr("info_message"));

            flixbusTripBOs.add(flixbusTripBO);
        }

        flixbusTripsVO.setFlixbusTripBOs(flixbusTripBOs);
        return flixbusTripsVO;
    }
}
