package com.EtiennePriou.go4launch.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class CheckDateTest {

    /**
     * Date is past so method return true
     */
    @Test
    public void isDatePastTrue() {
        Date date = new Date(1);
        assertTrue(CheckDate.isDatePast(date));
    }

    /**
     * Date is now so method return false
     */
    @Test
    public void isDatePastFalse() {
        Date date = new Date();
        assertFalse(CheckDate.isDatePast(date));
    }
}