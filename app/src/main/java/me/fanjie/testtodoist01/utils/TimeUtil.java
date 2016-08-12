package me.fanjie.testtodoist01.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fanjie on 2016/5/24.
 */
public class TimeUtil {

    public static String getSimpleDateToShow(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String date = sdf.format(calendar.getTime());

        String simpleDay;
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        int i = calendar.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
        switch (i){
            case -2:
                simpleDay = "前天";
                break;
            case -1:
                simpleDay = "昨天";
                break;
            case 0:
                simpleDay = "今天";
                break;
            case 1:
                simpleDay = "明天";
                break;
            case 2:
                simpleDay = "后天";
                break;
            default:
                return date;

        }
        return simpleDay + "(" + date + ")";
    }

}
