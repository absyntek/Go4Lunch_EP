package com.EtiennePriou.go4launch.services.map;

import android.content.Context;

import com.google.android.gms.maps.MapView;

public class DummyGoogleMapApiService implements GoogleMapApiService {

    MapView mMapView = null;
    Context mContext;

    public MapView getMapView() {
        return mMapView;
    }

    public void setMapView(MapView mapView) {
        mMapView = mapView;
    }
}
