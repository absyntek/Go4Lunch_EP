package com.EtiennePriou.go4launch.services.places;

import com.EtiennePriou.go4launch.BuildConfig;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.PlaceModel;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlacesApiServiceTest {

    private PlacesApi mPlacesApi;
    private List<PlaceModel> mPlaceModels;

    private final String googlePlaceUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
            "location=" + 123.0 + "," + 123.0 +
            "&radius=" + 10000 +
            "&type=restaurant" +
            "&sensor=true" +
            "&key=" + BuildConfig.PlaceApiKey;

    @Before
    public void setUp() throws Exception {
        mPlacesApi = DI.getServiceApiPlaces();
        toolForTest();
    }

    @Test
    public void getProximity_radius() {
        Integer PROXI_RAD = 10000;
        assertEquals(mPlacesApi.getProximity_radius(), PROXI_RAD);
    }

    @Test
    public void get_and_set_NearbyPlaceModelList() {
        mPlacesApi.setNearbyPlaceModelList(mPlaceModels);
        List<PlaceModel> placeModelList = mPlacesApi.getNearbyPlaceModelList();
        assertEquals(mPlaceModels.size(),placeModelList.size());
        assertThat(mPlaceModels, IsIterableContainingInAnyOrder.containsInAnyOrder(placeModelList.toArray()));
    }

    @Test
    public void getPlaceByReference() {
        mPlacesApi.setNearbyPlaceModelList(mPlaceModels);
        PlaceModel placeModel = mPlacesApi.getPlaceByReference("ref02");
        assertTrue(placeModel.equals(mPlaceModels.get(1)));
    }

    @Test
    public void setProximity_radius() {
        Integer integer = 10;
        mPlacesApi.setProximity_radius(integer);
        assertEquals(integer,mPlacesApi.getProximity_radius());
    }

    @Test
    public void setListPlaces() {
        //TODO ??
    }

    @Test
    public void get_and_set_UrlNearbyPlace() {
        mPlacesApi.setProximity_radius(10000);
        mPlacesApi.setUrlNearbyPlace(123,123);
        assertEquals(googlePlaceUrl, mPlacesApi.getUrlNearbyPlace());
    }

    private void toolForTest(){
        this.mPlaceModels = new ArrayList<>();
        this.mPlaceModels.add(new PlaceModel("name01","adresse01","ref01","imgRef01","id01","01","01","true"));
        this.mPlaceModels.add(new PlaceModel("name02","adresse02","ref02","imgRef02","id02","02","02","true"));
        this.mPlaceModels.add(new PlaceModel("name03","adresse03","ref03","imgRef03","id03","03","03","false"));
        this.mPlaceModels.add(new PlaceModel("name04","adresse04","ref04","imgRef04","id04","04","04","false"));
    }
}