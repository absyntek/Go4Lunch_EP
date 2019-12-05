package com.EtiennePriou.go4launch.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class CheckDate {
    public static Boolean isDatePast (Date dateToCheck){

        Date actualDate = new Date();

        String yearToCheck, yearActual, monthToCheck, monthActual, dayToCheck, dayActual;

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.FRANCE);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.FRANCE);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.FRANCE);

        yearToCheck = yearFormat.format(dateToCheck);
        yearActual = yearFormat.format(actualDate);

        monthToCheck = monthFormat.format(dateToCheck);
        monthActual = monthFormat.format(actualDate);

        dayToCheck = dayFormat.format(dateToCheck);
        dayActual = dayFormat.format(actualDate);


        return !(yearActual.equals(yearToCheck) && monthActual.equals(monthToCheck) && dayActual.equals(dayToCheck));
    }
}
