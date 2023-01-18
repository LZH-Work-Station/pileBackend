package com.pile.backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("blog")
@Data
public class Blog {
    @TableId(value = "id")
    private String id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "title")
    private String title;

    @TableField(value = "contenue")
    private String contenue;

    @TableField(value = "nombre_de_likes")
    private int nombreDeLikes;

    @TableField(value = "city")
    private String city;

    @TableField(value = "image")
    private String image;

    @TableField(value = "date")
    private Date date;
}
