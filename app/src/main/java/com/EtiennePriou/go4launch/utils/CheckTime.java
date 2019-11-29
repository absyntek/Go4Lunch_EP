package com.EtiennePriou.go4launch.utils;

import android.content.Context;
import android.os.Parcel;

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

    public static Map<String, Object> getStringTime (OpeningHours openingHours, Context context){
        Context mContext1 = context;
        List<Period> actualPeriod = new ArrayList<>();
        final Date date = new Date();
        String tosend;

        SimpleDateFormat formatterDay = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat formatterHours = new SimpleDateFormat("H", Locale.FRANCE);
        SimpleDateFormat formatterMinutes = new SimpleDateFormat("mm", Locale.FRANCE);

        String actualDay = formatterDay.format(date).toUpperCase();
        final int actualHour = Integer.parseInt(formatterHours.format(date));
        final int actualMinute = Integer.parseInt(formatterMinutes.format(date));

        LocalTime localTime = new LocalTime() {
            @Override
            public int getHours() {
                return actualHour;
            }

            @Override
            public int getMinutes() {
                return actualMinute;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        };

        for (Period period : openingHours.getPeriods()){
            if (period.getOpen().getDay().toString().equals(actualDay)){
                actualPeriod.add(period);
            }
        }

        List<Map<String, Object>> maps = new ArrayList<>();
        Map<String, Object> toReturn = new HashMap<>();

        if (!actualPeriod.isEmpty()){
            for (Period period : actualPeriod){
                Map<String, Object> toCreate = new HashMap<>();

                if (period.getOpen().getTime().compareTo(localTime)<0 && period.getClose().getTime().compareTo(localTime)>0){
                    tosend = mContext1.getResources().getString(R.string.openUntil) + period.getOpen().getTime().getHours() + ":" + formatterMinutes.format(period.getOpen().getTime().getMinutes());
                    toReturn.put("open", true);
                    toReturn.put("string", tosend);
                    return toReturn;
                }

                else if (period.getOpen().getTime().compareTo(localTime)<0 && period.getClose().getTime().compareTo(localTime)==0){
                    tosend = mContext1.getResources().getString(R.string.closingSoon);
                    toReturn.put("open", true);
                    toReturn.put("string", tosend);
                    return toReturn;
                }

                else if (period.getOpen().getTime().compareTo(localTime)>0 && period.getClose().getTime().compareTo(localTime)>0){

                    int tmpScore = period.getOpen().getTime().compareTo(localTime) + period.getClose().getTime().compareTo(localTime);
                    tosend = mContext1.getResources().getString(R.string.close_open_at) + period.getOpen().getTime().getHours() + ":" + formatterMinutes.format(period.getOpen().getTime().getMinutes());
                    toCreate.put("score", tmpScore);
                    toCreate.put("string", tosend);
                    maps.add(toCreate);
                }
            }

            if (maps.isEmpty()){
                toReturn.put("open", false);
                toReturn.put("string", mContext1.getResources().getString(R.string.close_for_rest_of));
                return toReturn;
            }
            else if (maps.size() == 1){
                toReturn.put("open", false);
                toReturn.put("string", String.valueOf(maps.get(0).get("string")));
                return toReturn;
            }else {
                Map good = null;
                for (Map map : maps){
                    if (good == null) good = map;
                    else if ((int) good.get("score") > (int) map.get("score")){
                        good = map;
                    }
                }
                toReturn.put("open", false);
                toReturn.put("string", String.valueOf(good.get("string")));
                return toReturn;
            }

        }else {
            toReturn.put("open", false);
            toReturn.put("string", mContext1.getResources().getString(R.string.closeToday));
            return toReturn;
        }
    }
}
