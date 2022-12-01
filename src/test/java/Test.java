import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pile.backend.PileApplication;
import com.pile.backend.common.util.RestfulRequestUtil;
import com.pile.backend.pojo.po.CarCo2;
import com.pile.backend.pojo.po.Gare;
import com.pile.backend.pojo.po.mapper.CarCo2Mapper;
import com.pile.backend.pojo.po.mapper.GareMapper;
import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PileApplication.class)
public class Test {
    private final static String url = "https://api.sncf.com/v1/coverage/sncf/journeys?to=admin%3Afr%3A69123&datetime_represents=departure&from=admin%3Afr%3A75056&datetime=20221114T055101".replaceAll("%3A", ":");

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private GareMapper gareMapper;

    @Autowired
    private CarCo2Mapper carCo2Mapper;

    @Autowired
    RestfulRequestUtil restfulRequestUtil;


    @org.junit.Test
    public void test1() throws ClassNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "3defecd3-7c95-487c-83de-1343cd951a20");
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);


        System.out.println(result.getBody());
        JSONObject jsonObject = JSONUtil.parseObj(result.getBody());
        System.out.println(jsonObject.getJSONArray("journeys").getJSONObject(0).getStr("arrival_date_time"));
        System.out.println(jsonObject.getJSONArray("journeys").getJSONObject(0).getStr("departure_date_time"));
        System.out.println(jsonObject.getJSONArray("journeys").getJSONObject(0).getJSONObject("co2_emission").getDouble("value"));
        System.out.println(jsonObject.getJSONArray("journeys").getJSONObject(0).getStr("duration"));
        System.out.println(jsonObject.getJSONArray("journeys").getJSONObject(0).getJSONArray("links").getJSONObject(0).getStr("href"));
    }

    @org.junit.Test
    public void testCo2Request(){
        String url = "https://api.distancematrix.ai/maps/api/distancematrix/json?origins=20 avenue albert einstein &destinations=part dieu&key=jiYcrAIIvKENNhfDJLx8FAfyTWwX8";
        JSONObject distanceInfo = restfulRequestUtil.doGet(url);
        JSONObject rows = (JSONObject) distanceInfo.getJSONArray("rows").get(0);
        System.out.println(rows);
        JSONObject elements = (JSONObject) rows.getJSONArray("elements").get(0);
        System.out.println(elements);
        Long distance = elements.getJSONObject("distance").getLong("value");
        System.out.println(distance);

    }


    @org.junit.Test
    public void testMyBatis() {
        File file = new File("src/main/resources/donneeInDatabase/liste-des-gares.csv");
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            reader.readLine();
            while ((tempString = reader.readLine()) != null) {
                Gare gare = new Gare();
                String[] strs = tempString.split(";");
                gare.setCodeUic(strs[0]);
                gare.setLibelle(strs[1]);
                gare.setFret(strs[2]);
                gare.setVoyageurs(strs[3]);
                gare.setCodeLigne(strs[4]);
                gare.setRgTroncon(Integer.parseInt(strs[5]));
                gare.setPk(strs[6]);
                gare.setCommune(strs[7]);
                gare.setDepartemen(strs[8]);
                gare.setIdreseau(strs[9]);
                gare.setIdgaia(strs[10]);
                gare.setXL93(Double.parseDouble(strs[11]));
                gare.setYL93(Double.parseDouble(strs[12]));
                gare.setXWgs84(Double.parseDouble(strs[13]));
                gare.setYWgs84(Double.parseDouble(strs[14]));
                gare.setCGeo(strs[15]);
                gare.setGeoPoint(strs[16]);
                gare.setGeoShape(strs[17]);
                gareMapper.insert(gare);
                System.out.println(gare);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    @org.junit.Test
    public void insetVoitureCo2() {
        File file = new File("src/main/resources/donneeInDatabase/vehicules-commercialises.csv");
        BufferedReader reader = null;
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            reader.readLine();
            while ((tempString = reader.readLine()) !=  null) {
                InsertCarInfo insertCarInfo = new InsertCarInfo();
                insertCarInfo.setCarCo2Mapper(carCo2Mapper);
                insertCarInfo.setTempString(tempString);
                pool.execute(insertCarInfo);
            }
            pool.shutdown();

            try {//等待直到所有任务完成
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


}

@Data
class InsertCarInfo implements Runnable{
    private String tempString;
    private CarCo2Mapper carCo2Mapper;

    @Override
    public void run() {
        CarCo2 carCo2 = new CarCo2();
        String[] strs = tempString.split(";");
        carCo2.setMarque(strs[0]);
        carCo2.setModeleDossier(strs[1]);
        carCo2.setModeleCommercial(strs[2]);
        carCo2.setDesignationCommerciale(strs[3]);
        carCo2.setCnit(strs[4]);
        carCo2.setTvv(strs[5]);
        carCo2.setCarburant(strs[6]);
        carCo2.setHybride(strs[7]);
        if(!testEmpty(strs[8])) carCo2.setPuissanceAdministrative(Integer.parseInt(strs[8]));
        if(!testEmpty(strs[9])) carCo2.setPuissanceMaximale(Double.parseDouble(strs[9]));
        carCo2.setBoiteDeVitesse(strs[10]);
        if(!testEmpty(strs[11])) carCo2.setConsommationUrbaine(Double.parseDouble(strs[11]));
        if(!testEmpty(strs[12])) carCo2.setConsommationExtraUrbaine(Double.parseDouble(strs[12]));
        if(!testEmpty(strs[13])) carCo2.setConsommationMixte(Double.parseDouble(strs[13]));
        if(!testEmpty(strs[14])) carCo2.setCo2(Double.parseDouble(strs[14]));
        if(!testEmpty(strs[15])) carCo2.setCoTypeI(Double.parseDouble(strs[15]));
        if(!testEmpty(strs[16])) carCo2.setHc(Double.parseDouble(strs[16]));
        if(!testEmpty(strs[17])) carCo2.setNox(Double.parseDouble(strs[17]));
        if(!testEmpty(strs[18])) carCo2.setHcNox(Double.parseDouble(strs[18]));
        if(!testEmpty(strs[19])) carCo2.setParticules(Double.parseDouble(strs[19]));
        if(!testEmpty(strs[20])) carCo2.setMasseVideEuroMin(Integer.parseInt(strs[20]));
        if(!testEmpty(strs[21])) carCo2.setMasseVideEuroMax(Integer.parseInt(strs[21]));
        carCo2.setChampV9(strs[22]);
        carCo2.setAnnee(strs[23]);
        carCo2.setCarrosserie(strs[24]);
        carCo2.setGamme(strs[25]);
        System.out.println(carCo2);
        carCo2Mapper.insert(carCo2);
        System.out.println(123);
    }

    private boolean testEmpty(String s){
        return "".equals(s);
    }
}
