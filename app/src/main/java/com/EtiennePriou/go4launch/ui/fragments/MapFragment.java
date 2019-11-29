package com.EtiennePriou.go4launch.ui.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.services.places.PlacesApi;

import com.EtiennePriou.go4launch.ui.MainViewModel;
import com.EtiennePriou.go4launch.ui.details.DetailPlaceActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener {

    private static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private static MainViewModel mMainViewModel;
    private GoogleMap mMap;
    private View mView;
    private Context mContext;
    private ProgressDialog myProgress;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesApi mPlacesApi;
    private PlacesClient placesClient;


    public MapFragment() { }

    public static MapFragment newInstance(MainViewModel mainViewModel) {
        MapFragment fragment = new MapFragment();
        mMainViewModel = mainViewModel;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mPlacesApi = DI.getServiceApiPlaces();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this.getContext()));

        //Configure PlacesClient
        if (mPlacesApi.getPlacesClient() == null){
            placesClient = Places.createClient(mContext);
            mPlacesApi.setPlacesClient(placesClient);
        }else {
            placesClient = mPlacesApi.getPlacesClient();
        }

        showProgressBar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = mView.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        requestPermissionLocation();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void requestPermissionLocation() {
        if (ContextCompat.checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // The Permissions to ask user.
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
            // Show a dialog asking the user to allow the above permissions.
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions, REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                setNearbyPlaces(location);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_ACCESS_COURSE_FINE_LOCATION) {
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getContext(), getString(R.string.PermissionGarented), Toast.LENGTH_LONG).show();
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        setNearbyPlaces(location);
                    }
                });
            }
            // Cancelled or denied.
            else {
                Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setNearbyPlaces(Location location) {

        if (mPlacesApi.getPlaces() == null){
            mPlacesApi.setLocation(location);
            mMainViewModel.setLocation(location);
            getPlacesAround();
        }else if (location.distanceTo(mPlacesApi.getLocation()) > 100){                              // si on se déplace de 100m
            mPlacesApi.setLocation(location);
            mMainViewModel.setLocation(location);
            getPlacesAround();
        }else {
            showNearbyPlaces();
        }
    }

    private void getPlacesAround(){
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.TYPES,
                Place.Field.ADDRESS,
                Place.Field.ID,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this.mContext, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()) {
                        FindCurrentPlaceResponse response = task.getResult();
                        if (response != null){
                            List<Place> placesList = takeOnlyRestaurant(response.getPlaceLikelihoods());
                            if (placesList.isEmpty()){
                                Toast.makeText(getContext(), mContext.getString(R.string.noplacefound), Toast.LENGTH_LONG).show();
                                myProgress.dismiss();
                            }else {
                                mPlacesApi.setPlaces(placesList);
                                showNearbyPlaces();
                            }
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            myProgress.dismiss();
                        }
                    }
                }
            });
        } else {
            requestPermissionLocation();
        }
    }

    private List<Place> takeOnlyRestaurant(List<PlaceLikelihood> placeLikelihoods) {
        List<Place> places = new ArrayList<>();
        for (PlaceLikelihood placeLikelihood : placeLikelihoods){
            if (placeLikelihood.getPlace().getTypes() != null &&
                    placeLikelihood.getPlace().getTypes().contains(Place.Type.RESTAURANT)){
                places.add(placeLikelihood.getPlace());
            }
        }
        return places;
    }

    private void showNearbyPlaces() {

        for (Place placeModel : mPlacesApi.getPlaces()) {

            MarkerOptions markerOptions = new MarkerOptions();

            if (Objects.requireNonNull(placeModel.getTypes()).contains(Place.Type.RESTAURANT)){

                markerOptions.position(Objects.requireNonNull(placeModel.getLatLng()));
                markerOptions.title(placeModel.getName());
                markerOptions.snippet(placeModel.getAddress());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker());

                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(placeModel.getId());
            }
        }
        LatLng latLng = new LatLng(mPlacesApi.getLocation().getLatitude(),mPlacesApi.getLocation().getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnInfoWindowClickListener(this);
        myProgress.dismiss();
    }

    private void showProgressBar() {
        // Create Progress Bar.
        myProgress = new ProgressDialog(mContext);
        myProgress.setTitle(getResources().getString(R.string.fetching_restaurant));
        myProgress.setMessage(getResources().getString(R.string.pleaseWait));
        myProgress.setCancelable(false);
        myProgress.setCanceledOnTouchOutside(false);
        // Display Progress Bar.
        myProgress.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        setNearbyPlaces(location);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent detailIntent = new Intent(this.getContext(), DetailPlaceActivity.class);
        detailIntent.putExtra(getString(R.string.PLACEREFERENCE), Objects.requireNonNull(marker.getTag()).toString());
        Objects.requireNonNull(this.getContext()).startActivity(detailIntent);
    }
}
