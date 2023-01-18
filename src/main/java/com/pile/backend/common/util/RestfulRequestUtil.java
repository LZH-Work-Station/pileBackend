package com.pile.backend.common.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pile.backend.controller.JourneyController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestfulRequestUtil {
    private static final Logger logger = LogManager.getLogger(RestfulRequestUtil.class);

    @Autowired
    RestTemplate restTemplate;

    public JSONObject doGet(String url, String token){
        // source: 75056, destination: 69123
        // 获取请求的url
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        ResponseEntity<String> result;
        try {
            result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        }catch (Exception e){
            logger.warn("Http request error " + e.getMessage());
            return null;
        }
        return JSONUtil.parseObj(result.getBody());
    }

    // 不需要token的请求
    public JSONObject doGet(String url){
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        return JSONUtil.parseObj(result.getBody());
    }

    public JSONArray doFlixbusSearchGet(String url){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", "25d27827demsh606a5a6643ae4f9p1c537ajsnf88fda967d9a");
        headers.add("X-RapidAPI-Host", "flixbus.p.rapidapi.com");
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        return JSONUtil.parseArray(result.getBody());
    }
}
