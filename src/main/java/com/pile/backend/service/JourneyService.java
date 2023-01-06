package com.pile.backend.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pile.backend.common.util.DateTimeUtil;
import com.pile.backend.common.util.RestfulRequestUtil;
import com.pile.backend.pojo.bo.JourneyBO;
import com.pile.backend.pojo.dto.GareRequestDTO;
import com.pile.backend.pojo.dto.JourneyRequestDTO;
import com.pile.backend.pojo.po.Gare;
import com.pile.backend.pojo.po.TarifSNCF;
import com.pile.backend.pojo.po.mapper.GareMapper;
import com.pile.backend.pojo.po.mapper.TarifSNCFMapper;
import com.pile.backend.pojo.vo.GareListVO;
import com.pile.backend.pojo.vo.JourneyListVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JourneyService {
    private static final Logger logger = LogManager.getLogger(JourneyService.class);
    private static final String url = "https://api.sncf.com/v1/coverage/sncf/journeys?from=stop_area:SNCF:{source}&to=stop_area:SNCF:{destination}&datetime={time}";
    private static final String token = "3defecd3-7c95-487c-83de-1343cd951a20";

    @Autowired
    RestfulRequestUtil restfulRequestUtil;

    @Autowired
    DateTimeUtil dateTimeUtil;

    @Autowired
    private GareMapper gareMapper;

    @Autowired
    private TarifSNCFMapper tarifSNCFMapper;

    public JourneyListVO getJourneyInfo(JourneyRequestDTO journeyRequestDTO) {
        // source: 75056, destination: 69123
        // 获取请求的url，判断是否为当天
        String now = dateTimeUtil.getTimeInFrance();    // 获得sncf API所需格式的当前时间
        String requestDate = journeyRequestDTO.getDate();
        int requestHour = journeyRequestDTO.getHour();
        String thisUrl = "";

        // 如果请求时间是过去就获取不到之前日期的数据
        if(dateTimeUtil.compare(requestDate, dateTimeUtil.getToday(), "yyyyMMdd", "yyyyMMdd")<0){
            return new JourneyListVO();
        }

        // 查询数据库构造url
        String sourceCode = getGareCode(journeyRequestDTO.getSource());
        String destinationCode = getGareCode(journeyRequestDTO.getDestination());


        // =-1代表没有定义时间
        if(requestHour==-1){
            if(now.substring(0, 8).equals(requestDate)){
                // 获取当前时间，因为出发日期是今天，所以从当前时间开始计算
                thisUrl = url.replace("{source}", sourceCode)
                        .replace("{destination}", destinationCode).replace("{time}", now);
            }else{
                // 获取指定日期的0点开始
                thisUrl = url.replace("{source}", sourceCode)
                        .replace("{destination}", destinationCode).replace("{time}", requestDate+"T000000");
            }
        }else{  // 定义了时间 直接按照定义的走
            String hour = "";
            if(requestHour < 10) {
                hour = "0" + requestHour;
            }else{
                hour = String.valueOf(requestHour);
            }
            thisUrl = url.replace("{source}", sourceCode)
                    .replace("{destination}", destinationCode).replace("{time}", requestDate+"T"+hour+"0000");
        }

        logger.info("Request sncf api on " + requestDate + " with url " + thisUrl);
        return buildListJourney(thisUrl, requestDate, journeyRequestDTO);
    }

    public JourneyListVO buildListJourney(String url, String date, JourneyRequestDTO journeyRequestDTO){
        List<JourneyBO> list = new ArrayList<>();
        requestJourney(url, date, journeyRequestDTO, list);
        JourneyListVO journeyListVO = new JourneyListVO();
        journeyListVO.setJourneyVOList(list);
        return journeyListVO;
    }

    public String getGareCode(String gareName){
        QueryWrapper<Gare> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.eq("libelle", gareName);
        sourceQueryWrapper.last("limit 1");
        List<Gare> gares = gareMapper.selectList(sourceQueryWrapper);
        if(gares.size()!=1){
            logger.error("No code of gare correspendance");
            return "";
        }else{
            return gares.get(0).getCodeUic();
        }
    }

    public void requestJourney(String url, String date, JourneyRequestDTO journeyRequestDTO, List<JourneyBO> list){
        JSONObject journeysInfo = restfulRequestUtil.doGet(url, token);
        JSONObject journeysInJson = journeysInfo.getJSONArray("journeys").getJSONObject(0);

        if(dateTimeUtil.compare(dateTimeUtil.changeFormatOfDate(journeysInJson.getStr("departure_date_time")), date, "yyyy-MM-dd HH:mm:ss", "yyyyMMdd")>0){
            // 创建Journey对象用来返回给前端
            JourneyBO journeyBO = new JourneyBO();
            journeyBO.setSource(journeyRequestDTO.getSource());
            journeyBO.setDestination(journeyRequestDTO.getDestination());
            journeyBO.setArrivalTime(dateTimeUtil.changeFormatOfDate(journeysInJson.getStr("arrival_date_time")));
            journeyBO.setDepartureTime(dateTimeUtil.changeFormatOfDate(journeysInJson.getStr("departure_date_time")));

            JSONArray sectionsInfo = journeysInJson.getJSONArray("sections");
            JSONObject mode = new JSONObject();
            // 为了获取运行的公司是TER还是啥的
            for(int i=0;i<sectionsInfo.size();i++){
                if(sectionsInfo.getJSONObject(i).containsKey("display_informations")){
                    mode = sectionsInfo.getJSONObject(i);
                }
            }
            String transporteur = mode.getJSONObject("display_informations").getStr("commercial_mode");
            String[] codes = getCodeUIC(url);
            double prix = getPrix(codes, transporteur.toLowerCase());
            journeyBO.setCommercialMode(transporteur);
            journeyBO.setCo2Emission(journeysInJson.getJSONObject("co2_emission").getDouble("value"));
            journeyBO.setDuration(journeysInJson.getInt("duration") / 60);
            journeyBO.setPrix(prix);
            list.add(journeyBO);
        }

        // 获得下一个请求的url
        String nextUrl = journeysInfo.getJSONArray("links").getJSONObject(0).getStr("href").replaceAll("%3A", ":");
        logger.info(nextUrl);
        // 如果日期不一致了就代表出发时间改变了
        if(date.equals(nextUrl.substring(nextUrl.length()-15, nextUrl.length()-7))){
            requestJourney(nextUrl, date, journeyRequestDTO, list);
        }
    }

    // 查询城市的火车站信息
    public GareListVO getGareInfo(GareRequestDTO gareRequestDTO) {
        QueryWrapper<Gare> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commune", gareRequestDTO.getCity().toUpperCase());
        queryWrapper.select("DISTINCT libelle");
        List<Gare> gareList = gareMapper.selectList(queryWrapper);
        GareListVO gareListVo = new GareListVO();
        List<String> namesOfGare = new ArrayList<>();
        for(Gare gare: gareList){
            namesOfGare.add(gare.getLibelle());
        }
        gareListVo.setGareList(namesOfGare);
        return gareListVo;
    }

    public String[] getCodeUIC(String url){
        String[] strs = url.split(":SNCF:");
        String[] codes = new String[2];
        for(int i=1;i<3;i++){
            codes[i-1] = strs[i].split("&")[0];
        }
        return codes;
    }

    public double getPrix(String[] codes, String transporteur){
        QueryWrapper<TarifSNCF> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("origine_code_uic", codes[0]);
        queryWrapper.eq("destination_code_uic", codes[1]);
        queryWrapper.eq("classe", 2);
        logger.info(transporteur + ", " + codes[0] + ", " + codes[1]);
        if(transporteur.equals("intercités")){
            transporteur = "intercites";
        }
        queryWrapper.eq("transporteur", transporteur);
        List<TarifSNCF> tarifSNCFS = tarifSNCFMapper.selectList(queryWrapper);
        if(tarifSNCFS.size()==0){
            return -1;
        }
        return tarifSNCFS.get(0).getPrix();
    }

}
