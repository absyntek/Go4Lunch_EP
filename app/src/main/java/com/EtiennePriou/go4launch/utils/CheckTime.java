package com.EtiennePriou.go4launch.utils;

import android.content.Context;

import com.EtiennePriou.go4launch.R;
import com.google.android.libraries.places.api.model.LocalTime;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Period;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CheckTime {

    public CheckTime() {
    }

    private static int actualMinute;
    private static int actualHour;

    public static Map<String, Object> getStringTime(OpeningHours openingHours, Context context){
        List<Period> actualPeriod = new ArrayList<>();
        final Date date = getDate();
        String tosend;

        SimpleDateFormat formatterDay = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat formatterHours = new SimpleDateFormat("H", Locale.FRANCE);
        SimpleDateFormat formatterMinutes = new SimpleDateFormat("mm", Locale.FRANCE);


        String actualDay = formatterDay.format(date).toUpperCase();
        actualHour = Integer.parseInt(formatterHours.format(date));
        actualMinute = Integer.parseInt(formatterMinutes.format(date));

        LocalTime localTime = LocalTime.newInstance(actualHour,actualMinute);

        for (Period period : openingHours.getPeriods()){
            if (period.getOpen().getDay().toString().equals(actualDay)){
                actualPeriod.add(period);
            }
        }

        List<Map<String, Object>> maps = new ArrayList<>();

        if (!actualPeriod.isEmpty()){
            for (Period period : actualPeriod){
                Map<String, Object> toCreate = new HashMap<>();

                int tmp = period.getClose().getTime().compareTo(localTime);

                if (period.getOpen().getTime().compareTo(localTime)<0 && period.getClose().getTime().compareTo(localTime)==1)
                {
                    tosend = context.getResources().getString(R.string.closingSoon);
                    return toReturnMap(tosend,true);
                }
                else if (period.getOpen().getTime().compareTo(localTime)<0 && period.getClose().getTime().compareTo(localTime)>0)
                {
                    tosend = context.getResources().getString(R.string.openUntil) + period.getOpen().getTime().getHours() + ":" + formatterMinutes.format(period.getOpen().getTime().getMinutes());
                    Map<String, Object> toReturn = new HashMap<>();
                    toReturn.put("open", true);
                    toReturn.put("string",tosend);
                    return toReturnMap(tosend,true);
                }
                else if (period.getOpen().getTime().compareTo(localTime)>0 && period.getClose().getTime().compareTo(localTime)>0)
                {
                    int tmpScore = period.getOpen().getTime().compareTo(localTime) + period.getClose().getTime().compareTo(localTime);
                    tosend = context.getResources().getString(R.string.close_open_at) + period.getOpen().getTime().getHours() + ":" + formatterMinutes.format(period.getOpen().getTime().getMinutes());
                    toCreate.put("score", tmpScore);
                    toCreate.put("string", tosend);
                    maps.add(toCreate);
                }
            }

            if (maps.isEmpty()){
                return toReturnMap(context.getResources().getString(R.string.close_for_rest_of),false);
            }
            else if (maps.size() == 1){
                return toReturnMap(String.valueOf(maps.get(0).get("string")),false);
            }else {
                Map good = null;
                for (Map map : maps){
                    if (good == null) good = map;
                    else if ((int) good.get("score") > (int) map.get("score")){
                        good = map;
                    }
                }
                return toReturnMap(String.valueOf(good.get("string")), false);
            }

        }else {
            return toReturnMap(context.getResources().getString(R.string.closeToday),false);
        }
    }

    public static Map<String, Object> toReturnMap(String s, Boolean aBoolean){
        Map<String, Object> toReturn = new HashMap<>();
        toReturn.put("open", aBoolean);
        toReturn.put("string",s);
        return toReturn;
    }


    public static Date getDate(){
        return new Date();
    }
}
