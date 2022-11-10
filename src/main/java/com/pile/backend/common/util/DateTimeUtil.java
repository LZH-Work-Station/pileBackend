package com.pile.backend.common.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.time.ZoneId;
import java.util.Date;

@Data
public class DateTimeUtil {
    // 获取法国的当前时间
    public String getTimeInFrance() {
        Date date = DateUtil.convertTimeZone(DateUtil.date(), ZoneId.of("Europe/Paris"));
        // 20221111093514
        String dateInUrl = DateUtil.format(date, "yyyyMMddHHmmss");
        StringBuilder sb = new StringBuilder(dateInUrl);
        sb.insert(8, "T");
        dateInUrl = sb.toString();
        return dateInUrl;
    }

    public String getToday(){
        Date date = DateUtil.convertTimeZone(DateUtil.date(), ZoneId.of("Europe/Paris"));
        // 20221111093514
        return DateUtil.format(date, "yyyyMMdd");
    }

    // 转换response里面的日期到正常输出格式
    public String changeFormatOfDate(String dateInUrl){
        String dateInString = dateInUrl.substring(0, 8) + " " + dateInUrl.substring(9);
        return DateUtil.format(DateUtil.parse(dateInString, "yyyyMMdd HHmmss"), "yyyy-MM-dd HH:mm:ss");
    }

    // 比较日期大小
    public int compare(String d1, String d2, String format1, String format2){
        Date d1OnDate = DateUtil.parse(d1, format1);
        Date d2OnDate = DateUtil.parse(d2, format2);
        return DateUtil.compare(d1OnDate, d2OnDate);
    }
}
