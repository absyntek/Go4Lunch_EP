package com.EtiennePriou.go4launch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class InternetTestTest {

    @Before
    private void setup (){
        Context context = Mockito.mock(Context.class);
        ConnectivityManager cm = Mockito.mock(ConnectivityManager.class);
        NetworkCapabilities capabilities = Mockito.mock(NetworkCapabilities.class);
        Mockito.when(context.getSystemService(Mockito.anyString())).thenReturn(cm);
        Mockito.when(cm != null).thenReturn(true);
        //Mockito.when()
    }
    @Test
    public void isNetworkConnected() {
    }
}