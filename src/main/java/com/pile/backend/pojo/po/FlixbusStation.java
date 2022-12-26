package com.pile.backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class FlixbusStation {

    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(value = "station_id")
    private String stationId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "address")
    private String address;

    @TableField(value = "latitude")
    private double latitude;

    @TableField(value = "longitude")
    private double longitude;

    @TableField(value = "city_id")
    private String cityId;

    @TableField(value = "city_name")
    private String cityName;
}
