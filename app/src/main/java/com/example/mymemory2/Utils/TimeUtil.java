package com.example.mymemory2.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    //获取当前日期
    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
