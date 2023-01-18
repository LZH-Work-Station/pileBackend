package com.pile.backend.service;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pile.backend.common.util.RestfulRequestUtil;
import com.pile.backend.pojo.bo.DistanceOfCarBO;
import com.pile.backend.pojo.dto.UploadBlogRequestDTO;
import com.pile.backend.pojo.dto.VoitureCo2RequestDTO;
import com.pile.backend.pojo.dto.VoitureMarqueListRequestDTO;
import com.pile.backend.pojo.dto.VoitureModelListRequestDTO;
import com.pile.backend.pojo.po.Blog;
import com.pile.backend.pojo.po.CarCo2;
import com.pile.backend.pojo.po.Gare;
import com.pile.backend.pojo.po.mapper.BlogMapper;
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

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogService {

    private static final Logger logger = LogManager.getLogger(BlogService.class);

    @Autowired
    private BlogMapper blogMapper;

    public String upload(UploadBlogRequestDTO uploadBlogRequestDTO){
        Blog blog = new Blog();
        String uuid = IdUtil.simpleUUID();
        blog.setCity(uploadBlogRequestDTO.getCity().toUpperCase());
        blog.setContenue(uploadBlogRequestDTO.getContenue());
        blog.setTitle(uploadBlogRequestDTO.getTitle());
        blog.setId(uuid);
        blog.setDate(new Date());
        blog.setImage(uploadBlogRequestDTO.getImage());
        blog.setUsername(uploadBlogRequestDTO.getUsername());
        blogMapper.insert(blog);
        logger.info("Upload the new blog", blog);
        return uuid;
    }

    public List<Blog> getLatestBlog(){
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("date");
        queryWrapper.last("limit 10");
        List<Blog> blogs = blogMapper.selectList(queryWrapper);
        return blogs;
    }

    public List<Blog> getCityLatestBlog(String city){
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("date");
        queryWrapper.eq("city", city);
        queryWrapper.last("limit 6");
        return blogMapper.selectList(queryWrapper);
    }
}
