package com.pile.backend.pojo.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("gare")
@Data
public class Gare {
    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(value = "code_uic")
    private String codeUic;

    @TableField(value = "libelle")
    private String libelle;

    @TableField(value = "fret")
    private String fret;

    @TableField(value = "voyageurs")
    private String voyageurs;

    @TableField(value = "code_ligne")
    private String codeLigne;

    @TableField(value = "rg_troncon")
    private int rgTroncon;

    @TableField(value = "pk")
    private String pk;

    @TableField(value = "commune")
    private String commune;

    @TableField(value = "departemen")
    private String departemen;

    @TableField(value = "idreseau")
    private String idreseau;

    @TableField(value = "idgaia")
    private String idgaia;

    @TableField(value = "x_l93")
    private double xL93;

    @TableField(value = "y_l93")
    private double yL93;

    @TableField(value = "x_wgs84")
    private double xWgs84;

    @TableField(value = "y_wgs84")
    private double  yWgs84;

    @TableField(value = "c_geo")
    private String cGeo;

    @TableField(value = "geo_point")
    private String geoPoint;

    @TableField(value = "geo_shape")
    private String geoShape;


}
