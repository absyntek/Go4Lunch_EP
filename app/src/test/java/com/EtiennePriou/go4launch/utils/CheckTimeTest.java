package com.EtiennePriou.go4launch.utils;

import android.content.Context;

import com.EtiennePriou.go4launch.R;
import com.google.android.libraries.places.api.model.DayOfWeek;
import com.google.android.libraries.places.api.model.LocalTime;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.TimeOfWeek;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

public class CheckTimeTest {

    OpeningHours openingHours;
    LocalTime localTime;
    Period mPeriod;
    Context context;

    @Before
    public void setUp (){
        openingHours = Mockito.mock(OpeningHours.class);
        localTime = Mockito.mock(LocalTime.class);
        TimeOfWeek timeOfWeek = Mockito.mock(TimeOfWeek.class);

        mPeriod = Mockito.mock(Period.class);
        List<Period> periods = new ArrayList<>();
        periods.add(mPeriod);

        Mockito.when(mPeriod.getOpen()).thenReturn(timeOfWeek);
        Mockito.when(timeOfWeek.getDay().toString()).thenReturn("MONDAY");
        Mockito.when(mPeriod.getOpen().getTime().getHours()).thenReturn(9);
        Mockito.when(mPeriod.getOpen().getTime().getMinutes()).thenReturn(10);


        // Mock Context
        context = Mockito.mock(Context.class);

        Mockito.when(context.getResources().getString(R.string.closingSoon)).thenReturn("2");
        Mockito.when(context.getResources().getString(R.string.close_open_at)).thenReturn("3");
        Mockito.when(context.getResources().getString(R.string.close_for_rest_of)).thenReturn("4");
        Mockito.when(context.getResources().getString(R.string.closeToday)).thenReturn("5");
    }

    @Test
    public void getStringTime() {

        Mockito.when(context.getResources().getString(R.string.openUntil)).thenReturn("1");

        Mockito.when(mPeriod.getOpen().getTime().compareTo(Mockito.any(LocalTime.class))).thenReturn(-1);
        Mockito.when(mPeriod.getClose().getTime().compareTo(Mockito.any(LocalTime.class))).thenReturn(1);

        Map<String,Object> stringObjectMap;
        stringObjectMap = CheckTime.getStringTime(openingHours,context);
        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");
        assertTrue(s.equals("19:10"));
        assertTrue(aBoolean);

    }
}