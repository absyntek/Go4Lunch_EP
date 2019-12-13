package com.EtiennePriou.go4launch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckDate {
    public static Boolean isDatePast (Date dateToCheck){

        Date actualDate = new Date();

        String strToCheck, strActual;

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.FRANCE);

        strToCheck = format.format(dateToCheck);
        strActual = format.format(actualDate);

        return !(strActual.equals(strToCheck));
    }
}
