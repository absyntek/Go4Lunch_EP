package com.EtiennePriou.go4launch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class InternetTestTest {

    Context context;
    ConnectivityManager cm;
    NetworkCapabilities capabilities;


    @Before
    public void setup (){
        context = Mockito.mock(Context.class);
        cm = Mockito.mock(ConnectivityManager.class);
        capabilities = Mockito.mock(NetworkCapabilities.class);
        Mockito.when(context.getSystemService(Mockito.anyString())).thenReturn(cm);
        Mockito.when(cm != null).thenReturn(true);
        //Mockito.when()
    }
    @Test
    public void isNetworkConnected() {
    }
}