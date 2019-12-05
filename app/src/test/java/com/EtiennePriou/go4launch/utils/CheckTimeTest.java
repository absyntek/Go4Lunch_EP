package com.EtiennePriou.go4launch.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.EtiennePriou.go4launch.R;
import com.google.android.libraries.places.api.model.DayOfWeek;
import com.google.android.libraries.places.api.model.LocalTime;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Period;
import com.google.android.libraries.places.api.model.TimeOfWeek;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class CheckTimeTest {

    private OpeningHours openingHours;
    private Context context;

    @Before
    public void setUp (){

        buildOpeningHours();

        context = Mockito.mock(Context.class);
        Resources resources = Mockito.mock(Resources.class);
        when(context.getResources()).thenReturn(resources);

        when(resources.getString(R.string.openUntil)).thenReturn("1");
        when(resources.getString(R.string.closingSoon)).thenReturn("2");
        when(resources.getString(R.string.close_open_at)).thenReturn("3");
        when(resources.getString(R.string.close_for_rest_of)).thenReturn("4");
        when(resources.getString(R.string.closeToday)).thenReturn("5");
    }

    @Test
    public void getStringTime() {

        Map<String,Object> stringObjectMap;

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertTrue(s.equals("5"));
        assertFalse(aBoolean);
    }

    private void buildOpeningHours (){
        Period MONDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.MONDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.MONDAY,LocalTime.newInstance(15,0)))
                .build();
        Period TUESDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.TUESDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.TUESDAY,LocalTime.newInstance(15,0)))
                .build();
        Period WEDNESDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.WEDNESDAY,LocalTime.newInstance(11,30)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.WEDNESDAY,LocalTime.newInstance(15,0)))
                .build();
        Period THURSDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.THURSDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.THURSDAY,LocalTime.newInstance(15,0)))
                .build();
        Period FRIDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.FRIDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.FRIDAY,LocalTime.newInstance(15,0)))
                .build();
        Period SATURDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.SATURDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.SATURDAY,LocalTime.newInstance(15,0)))
                .build();
        Period SUNDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.SUNDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.SUNDAY,LocalTime.newInstance(15,0)))
                .build();

        final List<Period> mPeriods = Arrays.asList(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY);
        final List<String> mStrings = Arrays.asList("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY");

        openingHours = new OpeningHours() {
            @NonNull
            @Override
            public List<Period> getPeriods() {
                return mPeriods;
            }

            @NonNull
            @Override
            public List<String> getWeekdayText() {
                return mStrings;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        };
    }
    private void mockContext() {
        context = Mockito.mock(Context.class);
        Resources resources = Mockito.mock(Resources.class);
        when(context.getResources()).thenReturn(resources);

        when(resources.getString(R.string.openUntil)).thenReturn("1");
        when(resources.getString(R.string.closingSoon)).thenReturn("2");
        when(resources.getString(R.string.close_open_at)).thenReturn("3");
        when(resources.getString(R.string.close_for_rest_of)).thenReturn("4");
        when(resources.getString(R.string.closeToday)).thenReturn("5");
    }
}