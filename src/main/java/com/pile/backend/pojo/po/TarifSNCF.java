package com.pile.backend.pojo.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tarif_sncf")
@Data
public class TarifSNCF {
    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(value = "transporteur")
    private String transporteur;

    @TableField(value = "origine")
    private String origine;

    @TableField(value = "origine_code_uic")
    private String origineCodeUIC;

    @TableField(value = "destination")
    private String destination;

    @TableField(value = "destination_code_uic")
    private String destinationCodeUIC;

    @TableField(value = "classe")
    private int classe;

    @TableField(value = "prix")
    private double prix;
}
