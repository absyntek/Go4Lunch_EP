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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CheckTime.class)
public class CheckTimeTest {

    private OpeningHours openingHours;
    private Context context;

    private Date date = null;

    private SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.yyyy.HH.mm");

    private Map<String,Object> stringObjectMap;

    @Before
    public void setUp (){

        buildOpeningHours();

        context = Mockito.mock(Context.class);
        Resources resources = Mockito.mock(Resources.class);
        Mockito.when(context.getResources()).thenReturn(resources);

        Mockito.when(resources.getString(R.string.openUntil)).thenReturn("1");
        Mockito.when(resources.getString(R.string.closingSoon)).thenReturn("2");
        Mockito.when(resources.getString(R.string.close_open_at)).thenReturn("3");
        Mockito.when(resources.getString(R.string.close_for_rest_of)).thenReturn("4");
        Mockito.when(resources.getString(R.string.closeToday)).thenReturn("5");

        PowerMockito.mockStatic(CheckTime.class);
        PowerMockito.when(CheckTime.getStringTime(openingHours,context)).thenCallRealMethod();
        PowerMockito.when(CheckTime.toReturnMap(Mockito.anyString(),Mockito.anyBoolean())).thenCallRealMethod();
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
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.WEDNESDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.WEDNESDAY,LocalTime.newInstance(15,0)))
                .build();
        Period THURSDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.THURSDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.THURSDAY,LocalTime.newInstance(15,0)))
                .build();
        Period THURSDAYbis = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.THURSDAY,LocalTime.newInstance(18,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.THURSDAY,LocalTime.newInstance(23,0)))
                .build();
        Period FRIDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.FRIDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.FRIDAY,LocalTime.newInstance(15,0)))
                .build();
        Period FRIDAYbis = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.FRIDAY,LocalTime.newInstance(18,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.FRIDAY,LocalTime.newInstance(23,0)))
                .build();
        Period SATURDAY = Period.builder()
                .setOpen(TimeOfWeek.newInstance(DayOfWeek.SATURDAY,LocalTime.newInstance(12,0)))
                .setClose(TimeOfWeek.newInstance(DayOfWeek.SATURDAY,LocalTime.newInstance(15,0)))
                .build();

        final List<Period> mPeriods = Arrays.asList(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,THURSDAYbis,FRIDAY,FRIDAYbis,SATURDAY);
        final List<String> mStrings = Arrays.asList("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","THURSDAY","FRIDAY","FRIDAY","SATURDAY");

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

    @Test
    public void getStringTime_Monday() {

        try {
            String monday = "02.12.2019.13.00";
            date = mFormat.parse(monday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PowerMockito.when(CheckTime.getDate()).thenReturn(date);

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertTrue(s.equals("112:00"));
        assertTrue(aBoolean);
    }

    @Test
    public void getStringTime_Tuesday() {

        try {
            String tuesday = "03.12.2019.09.30";
            date = mFormat.parse(tuesday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PowerMockito.when(CheckTime.getDate()).thenReturn(date);

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertEquals("312:00", s);
        assertFalse(aBoolean);
    }

    @Test
    public void getStringTime_Wednesday() {

        try {
            String wednesday = "04.12.2019.14.50";
            date = mFormat.parse(wednesday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PowerMockito.when(CheckTime.getDate()).thenReturn(date);

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertEquals("2", s);
        assertTrue(aBoolean);
    }

    @Test
    public void getStringTime_Thursday() {

        try {
            String thursday = "05.12.2019.9.30";
            date = mFormat.parse(thursday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PowerMockito.when(CheckTime.getDate()).thenReturn(date);

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertEquals("312:00", s);
        assertFalse(aBoolean);
    }

    @Test
    public void getStringTime_Friday() {

        try {
            String friday = "06.12.2019.15.30";
            date = mFormat.parse(friday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PowerMockito.when(CheckTime.getDate()).thenReturn(date);

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertEquals("318:00", s);
        assertFalse(aBoolean);
    }

    @Test
    public void getStringTime_Saturday() {

        try {
            String saturday = "07.12.2019.19.30";
            date = mFormat.parse(saturday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PowerMockito.when(CheckTime.getDate()).thenReturn(date);

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertEquals("4", s);
        assertFalse(aBoolean);
    }

    @Test
    public void getStringTime_Sunday() {

        try {
            String sunday = "08.12.2019.15.30";
            date = mFormat.parse(sunday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PowerMockito.when(CheckTime.getDate()).thenReturn(date);

        stringObjectMap = CheckTime.getStringTime(openingHours,context);

        Boolean aBoolean = (Boolean) stringObjectMap.get("open");
        String s = (String) stringObjectMap.get("string");

        assertEquals("5", s);
        assertFalse(aBoolean);
    }
}