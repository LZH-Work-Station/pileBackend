package com.pile.backend.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pile.backend.common.util.RestfulRequestUtil;
import com.pile.backend.pojo.bo.DistanceOfCarBO;
import com.pile.backend.pojo.dto.VoitureCo2RequestDTO;
import com.pile.backend.pojo.dto.VoitureMarqueListRequestDTO;
import com.pile.backend.pojo.dto.VoitureModelListRequestDTO;
import com.pile.backend.pojo.po.CarCo2;
import com.pile.backend.pojo.po.Gare;
import com.pile.backend.pojo.po.mapper.CarCo2Mapper;
import com.pile.backend.pojo.po.mapper.GareMapper;
import com.pile.backend.pojo.vo.VoitureCo2AndDistVO;
import com.pile.backend.pojo.vo.VoitureMarqueListVO;
import com.pile.backend.pojo.vo.VoitureModelListVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoitureService {

    private static final Logger logger = LogManager.getLogger(VoitureService.class);
    private static final String url = "https://api.distancematrix.ai/maps/api/distancematrix/json?origins={source}&destinations={destination}&key={token}";
    private static final String token = "jiYcrAIIvKENNhfDJLx8FAfyTWwX8";

    @Autowired
    private CarCo2Mapper carCo2Mapper;

    @Autowired
    private RestfulRequestUtil restfulRequestUtil;

    public VoitureCo2AndDistVO getVoitureCo2AndDist(VoitureCo2RequestDTO voitureCo2RequestDTO) {
        String source = voitureCo2RequestDTO.getSource();
        String destination = voitureCo2RequestDTO.getDestination();
        String marque = voitureCo2RequestDTO.getMarque();
        String modele = voitureCo2RequestDTO.getModele();

        String requestUrl = url.replace("{source}", source)
                .replace("{destination}", destination).replace("{token}", token);

        // 查询汽车牌子所需要的CO2(g/km)
        double co2Emission = getCo2EmissionOfCar(marque, modele);

        // 获得距离
        DistanceOfCarBO distanceOfCarBO = getDistanceOfCarBO(requestUrl);

        VoitureCo2AndDistVO voitureCo2AndDistVO = new VoitureCo2AndDistVO();
        voitureCo2AndDistVO.setSource(distanceOfCarBO.getSource());
        voitureCo2AndDistVO.setDestination(distanceOfCarBO.getDestination());
        voitureCo2AndDistVO.setCo2Emission(co2Emission * distanceOfCarBO.getDistance());
        voitureCo2AndDistVO.setDistance(distanceOfCarBO.getDistance());
        return voitureCo2AndDistVO;
    }


    public VoitureMarqueListVO getVoitureMarqueList(VoitureMarqueListRequestDTO voitureMarqueListRequestDTO) {
        QueryWrapper<CarCo2> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.likeRight("marque", voitureMarqueListRequestDTO.getMarque().toUpperCase());
        sourceQueryWrapper.select("DISTINCT marque");
        List<CarCo2> carCo2s = carCo2Mapper.selectList(sourceQueryWrapper);
        List<String> marques = new ArrayList<>();
        for(CarCo2 carCo2: carCo2s){
            marques.add(carCo2.getMarque());
        }
        VoitureMarqueListVO voitureMarqueListVO = new VoitureMarqueListVO();
        voitureMarqueListVO.setMarques(marques);
        return voitureMarqueListVO;
    }

    public VoitureModelListVO getVoitureModelList(VoitureModelListRequestDTO voitureModelListRequestDTO) {
        QueryWrapper<CarCo2> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.eq("marque", voitureModelListRequestDTO.getMarque());
        sourceQueryWrapper.likeRight("modele_commercial", voitureModelListRequestDTO.getModele().toUpperCase());
        sourceQueryWrapper.select("DISTINCT modele_commercial");
        List<CarCo2> carCo2s = carCo2Mapper.selectList(sourceQueryWrapper);
        List<String> modeles = new ArrayList<>();
        for(CarCo2 carCo2: carCo2s){
            modeles.add(carCo2.getModeleCommercial());
        }
        VoitureModelListVO voitureModelListVO = new VoitureModelListVO();
        voitureModelListVO.setModeles(modeles);
        return voitureModelListVO;
    }


    // 获得每公里汽车排放量
    public double getCo2EmissionOfCar(String marque, String modele){
        QueryWrapper<CarCo2> sourceQueryWrapper = new QueryWrapper<>();
        sourceQueryWrapper.eq("marque", marque);
        sourceQueryWrapper.eq("modele_commercial", modele);
        sourceQueryWrapper.last("limit 1");
        List<CarCo2> carCo2s = carCo2Mapper.selectList(sourceQueryWrapper);

        double sum = 0.0;
        for(CarCo2 carCo2: carCo2s){
            sum += carCo2.getCo2();
        }

        return sum/carCo2s.size();
    }


    // 获得汽车距离信息
    public DistanceOfCarBO getDistanceOfCarBO(String url){
        logger.info("Request for distance of car by url: " + url);
        JSONObject distanceInfo = restfulRequestUtil.doGet(url);
        String destinationComplet = (String) distanceInfo.getJSONArray("destination_addresses").get(0);
        String sourceComplet = (String) distanceInfo.getJSONArray("origin_addresses").get(0);
        JSONObject rows = (JSONObject) distanceInfo.getJSONArray("rows").get(0);
        JSONObject elements = (JSONObject) rows.getJSONArray("elements").get(0);
        Long distance = elements.getJSONObject("distance").getLong("value");
        logger.info("Get result from : " + sourceComplet + ", " + "to : " + destinationComplet + ", in " + distance + " km");


        // 返回单位为km
        DistanceOfCarBO distanceOfCarBO = new DistanceOfCarBO();
        distanceOfCarBO.setSource(sourceComplet);
        distanceOfCarBO.setDestination(destinationComplet);
        distanceOfCarBO.setDistance(distance/1000.0);
        return distanceOfCarBO;
    }
}
