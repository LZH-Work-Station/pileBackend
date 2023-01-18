package com.pile.backend.controller;

import com.pile.backend.common.result.Result;
import com.pile.backend.pojo.dto.CityStationsRequestDTO;
import com.pile.backend.pojo.dto.FlixbusTripsRequestDTO;
import com.pile.backend.pojo.dto.UploadBlogRequestDTO;
import com.pile.backend.pojo.vo.CityStationsVO;
import com.pile.backend.pojo.vo.FlixbusTripsVO;
import com.pile.backend.service.BlogService;
import com.pile.backend.service.FlixbusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/blog")
@CrossOrigin
public class BlogController {
    @Autowired
    private BlogService blogService;
    private static final Logger logger = LogManager.getLogger(FlixbusController.class);

    @PostMapping("upload")
    @ApiOperation(value = "上传博客")
    public Result uploadBlog(@RequestBody UploadBlogRequestDTO uploadBlogRequestDTO){
        try{
            logger.info("Request to upload : " + uploadBlogRequestDTO.toString());
            String uuid = blogService.upload(uploadBlogRequestDTO);
            return Result.ok(uuid);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("getLatestBlog")
    @ApiOperation(value = "获得最新的blog")
    public Result getLatestBlog(){
        try{
            return Result.ok(blogService.getLatestBlog());
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("getCityLatestBlog")
    @ApiOperation(value = "获得某个城市最新的blog")
    public Result getCityLatestBlog(@RequestBody String city){
        try{
            return Result.ok(blogService.getCityLatestBlog(city));
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }

}
