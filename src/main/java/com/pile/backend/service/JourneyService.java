package com.pile.backend.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.pile.backend.common.util.DateTimeUtil;
import com.pile.backend.common.util.RestfulRequestUtil;
import com.pile.backend.pojo.dto.Journey;
import com.pile.backend.pojo.dto.JourneyRequestDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JourneyService {
    private static final Logger logger = LogManager.getLogger(JourneyService.class);
    private static final String url = "https://api.sncf.com/v1/coverage/sncf/journeys?from={source}&to={destination}&datetime={time}";
    private static final String token = "3defecd3-7c95-487c-83de-1343cd951a20";

    @Autowired
    RestfulRequestUtil restfulRequestUtil;

    @Autowired
    DateTimeUtil dateTimeUtil;

    public List<Journey> getJourneyInfo(JourneyRequestDTO journeyRequestDTO) {
        // source: 75056, destination: 69123
        // 获取请求的url，判断是否为当天
        String now = dateTimeUtil.getTimeInFrance();    // 获得sncf API所需格式的当前时间
        String requestDate = journeyRequestDTO.getDate();
        int requestHour = journeyRequestDTO.getHour();
        String thisUrl = "";

        // 如果请求时间是过去就获取不到之前日期的数据
        if(dateTimeUtil.compare(requestDate, dateTimeUtil.getToday(), "yyyyMMdd", "yyyyMMdd")<0){
            return new ArrayList<>();
        }

        // =-1代表没有定义时间
        if(requestHour==-1){
            if(now.substring(0, 8).equals(requestDate)){
                // 获取当前时间，因为出发日期是今天，所以从当前时间开始计算
                thisUrl = url.replace("{source}", journeyRequestDTO.getSource())
                        .replace("{destination}", journeyRequestDTO.getDestination()).replace("{time}", now);
            }else{
                // 获取指定日期的0点开始
                thisUrl = url.replace("{source}", journeyRequestDTO.getSource())
                        .replace("{destination}", journeyRequestDTO.getDestination()).replace("{time}", requestDate+"T000000");
            }
        }else{  // 定义了时间 直接按照定义的走
            String hour = "";
            if(requestHour < 10) {
                hour = "0" + requestHour;
            }else{
                hour = String.valueOf(requestHour);
            }
            thisUrl = url.replace("{source}", journeyRequestDTO.getSource())
                    .replace("{destination}", journeyRequestDTO.getDestination()).replace("{time}", requestDate+"T"+hour+"0000");
        }

        logger.info("Request sncf api on " + requestDate + " with url " + thisUrl);
        return buildListJourney(thisUrl, requestDate, journeyRequestDTO);
    }

    public List<Journey> buildListJourney(String url, String date, JourneyRequestDTO journeyRequestDTO){
        List<Journey> list = new ArrayList<>();
        requestJourney(url, date, journeyRequestDTO, list);
        return list;
    }

    public void requestJourney(String url, String date, JourneyRequestDTO journeyRequestDTO, List<Journey> list){
        JSONObject journeysInfo = restfulRequestUtil.doGet(url, token);
        JSONObject journeysInJson = journeysInfo.getJSONArray("journeys").getJSONObject(0);

        if(dateTimeUtil.compare(dateTimeUtil.changeFormatOfDate(journeysInJson.getStr("departure_date_time")), date, "yyyy-MM-dd HH:mm:ss", "yyyyMMdd")>0){
            // 创建Journey对象用来返回给前端
            Journey journey = new Journey();
            journey.setSource(journeyRequestDTO.getSource());
            journey.setDestination(journeyRequestDTO.getDestination());
            journey.setArrivalTime(dateTimeUtil.changeFormatOfDate(journeysInJson.getStr("arrival_date_time")));
            journey.setDepartureTime(dateTimeUtil.changeFormatOfDate(journeysInJson.getStr("departure_date_time")));
            journey.setCo2Emission(journeysInJson.getJSONObject("co2_emission").getDouble("value"));
            journey.setDuration(journeysInJson.getInt("duration") / 60);
            list.add(journey);
        }

        // 获得下一个请求的url
        String nextUrl = journeysInfo.getJSONArray("links").getJSONObject(0).getStr("href").replaceAll("%3A", ":");

        // 如果日期不一致了就代表出发时间改变了
        if(date.equals(nextUrl.substring(nextUrl.length()-15, nextUrl.length()-7))){
            requestJourney(nextUrl, date, journeyRequestDTO, list);
        }
    }
}
