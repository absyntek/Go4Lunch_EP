package com.EtiennePriou.go4launch.utils;

import android.net.Uri;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.PlaceToGo;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.places.PlacesApi;
import com.google.android.gms.internal.firebase_auth.zzff;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.zzy;
import com.google.firebase.auth.zzz;

import java.util.Arrays;
import java.util.List;

public class PoppulateAPis {

    private static FirebaseUser mFirebaseUser;
    private static List<Workmate> mWorkmateList;
    private static List<Place> places;


    public static void popApi (){
        FireBaseApi fireBaseApi = DI.getServiceFireBase();
        PlacesApi placesApi = DI.getServiceApiPlaces();

        setWorkmates();
        setPlaces();

        fireBaseApi.setCurrentUser(mFirebaseUser);
        fireBaseApi.setActualUser(mWorkmateList.get(0));
        placesApi.setPlaces(places);
    }

    private static void setWorkmates() {
        mFirebaseUser = new FirebaseUser() {
            @NonNull
            @Override
            public String getUid() {
                return "123456";
            }

            @NonNull
            @Override
            public String getProviderId() {
                return "987654";
            }

            @Override
            public boolean isAnonymous() {
                return false;
            }

            @Nullable
            @Override
            public List<String> zza() {
                return null;
            }

            @NonNull
            @Override
            public List<? extends UserInfo> getProviderData() {
                return null;
            }

            @NonNull
            @Override
            public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
                return null;
            }

            @Override
            public FirebaseUser zzb() {
                return null;
            }

            @NonNull
            @Override
            public FirebaseApp zzc() {
                return null;
            }

            @Nullable
            @Override
            public String getDisplayName() {
                return "Test";
            }

            @Nullable
            @Override
            public Uri getPhotoUrl() {
                return null;
            }

            @Nullable
            @Override
            public String getEmail() {
                return "test@tt.com";
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @Nullable
            @Override
            public String zzd() {
                return null;
            }

            @NonNull
            @Override
            public zzff zze() {
                return null;
            }

            @Override
            public void zza(@NonNull zzff zzff) {

            }

            @NonNull
            @Override
            public String zzf() {
                return null;
            }

            @NonNull
            @Override
            public String zzg() {
                return null;
            }

            @Nullable
            @Override
            public FirebaseUserMetadata getMetadata() {
                return null;
            }

            @NonNull
            @Override
            public zzz zzh() {
                return null;
            }

            @Override
            public void zzb(List<zzy> list) {

            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }

            @Override
            public boolean isEmailVerified() {
                return false;
            }
        };
        PlaceToGo placeToGo = new PlaceToGo("000", "000", "res000");
        Workmate workmate0 = new Workmate("000","000",null,null);
        Workmate workmate1 = new Workmate("111","111",null,null);
        Workmate workmate2 = new Workmate("222","222",null, placeToGo);
        Workmate workmate3 = new Workmate("333","333",null, placeToGo);
        mWorkmateList = Arrays.asList(workmate0,workmate1,workmate2,workmate3);
    }

    private static void setPlaces (){
        Place place0 = new Place() {
            @Nullable
            @Override
            public String getAddress() {
                return "000";
            }

            @Nullable
            @Override
            public AddressComponents getAddressComponents() {
                return null;
            }

            @Nullable
            @Override
            public List<String> getAttributions() {
                return null;
            }

            @Nullable
            @Override
            public String getId() {
                return "res000";
            }

            @Nullable
            @Override
            public LatLng getLatLng() {
                return new LatLng(1,1);
            }

            @Nullable
            @Override
            public String getName() {
                return "000";
            }

            @Nullable
            @Override
            public OpeningHours getOpeningHours() {
                return null;
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @Nullable
            @Override
            public List<PhotoMetadata> getPhotoMetadatas() {
                return null;
            }

            @Nullable
            @Override
            public PlusCode getPlusCode() {
                return null;
            }

            @Nullable
            @Override
            public Integer getPriceLevel() {
                return null;
            }

            @Nullable
            @Override
            public Double getRating() {
                return null;
            }

            @Nullable
            @Override
            public List<Type> getTypes() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUserRatingsTotal() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUtcOffsetMinutes() {
                return null;
            }

            @Nullable
            @Override
            public LatLngBounds getViewport() {
                return null;
            }

            @Nullable
            @Override
            public Uri getWebsiteUri() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        };
        Place place1 = new Place() {
            @Nullable
            @Override
            public String getAddress() {
                return "111";
            }

            @Nullable
            @Override
            public AddressComponents getAddressComponents() {
                return null;
            }

            @Nullable
            @Override
            public List<String> getAttributions() {
                return null;
            }

            @Nullable
            @Override
            public String getId() {
                return "res111";
            }

            @Nullable
            @Override
            public LatLng getLatLng() {
                return new LatLng(1,1);
            }

            @Nullable
            @Override
            public String getName() {
                return "111";
            }

            @Nullable
            @Override
            public OpeningHours getOpeningHours() {
                return null;
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @Nullable
            @Override
            public List<PhotoMetadata> getPhotoMetadatas() {
                return null;
            }

            @Nullable
            @Override
            public PlusCode getPlusCode() {
                return null;
            }

            @Nullable
            @Override
            public Integer getPriceLevel() {
                return null;
            }

            @Nullable
            @Override
            public Double getRating() {
                return null;
            }

            @Nullable
            @Override
            public List<Type> getTypes() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUserRatingsTotal() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUtcOffsetMinutes() {
                return null;
            }

            @Nullable
            @Override
            public LatLngBounds getViewport() {
                return null;
            }

            @Nullable
            @Override
            public Uri getWebsiteUri() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        };
        Place place2 = new Place() {
            @Nullable
            @Override
            public String getAddress() {
                return "222";
            }

            @Nullable
            @Override
            public AddressComponents getAddressComponents() {
                return null;
            }

            @Nullable
            @Override
            public List<String> getAttributions() {
                return null;
            }

            @Nullable
            @Override
            public String getId() {
                return "res222";
            }

            @Nullable
            @Override
            public LatLng getLatLng() {
                return new LatLng(1,1);
            }

            @Nullable
            @Override
            public String getName() {
                return "222";
            }

            @Nullable
            @Override
            public OpeningHours getOpeningHours() {
                return null;
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @Nullable
            @Override
            public List<PhotoMetadata> getPhotoMetadatas() {
                return null;
            }

            @Nullable
            @Override
            public PlusCode getPlusCode() {
                return null;
            }

            @Nullable
            @Override
            public Integer getPriceLevel() {
                return null;
            }

            @Nullable
            @Override
            public Double getRating() {
                return null;
            }

            @Nullable
            @Override
            public List<Type> getTypes() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUserRatingsTotal() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUtcOffsetMinutes() {
                return null;
            }

            @Nullable
            @Override
            public LatLngBounds getViewport() {
                return null;
            }

            @Nullable
            @Override
            public Uri getWebsiteUri() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        };
        Place place3 = new Place() {
            @Nullable
            @Override
            public String getAddress() {
                return "333";
            }

            @Nullable
            @Override
            public AddressComponents getAddressComponents() {
                return null;
            }

            @Nullable
            @Override
            public List<String> getAttributions() {
                return null;
            }

            @Nullable
            @Override
            public String getId() {
                return "res333";
            }

            @Nullable
            @Override
            public LatLng getLatLng() {
                return new LatLng(1,1);
            }

            @Nullable
            @Override
            public String getName() {
                return "333";
            }

            @Nullable
            @Override
            public OpeningHours getOpeningHours() {
                return null;
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @Nullable
            @Override
            public List<PhotoMetadata> getPhotoMetadatas() {
                return null;
            }

            @Nullable
            @Override
            public PlusCode getPlusCode() {
                return null;
            }

            @Nullable
            @Override
            public Integer getPriceLevel() {
                return null;
            }

            @Nullable
            @Override
            public Double getRating() {
                return null;
            }

            @Nullable
            @Override
            public List<Type> getTypes() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUserRatingsTotal() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUtcOffsetMinutes() {
                return null;
            }

            @Nullable
            @Override
            public LatLngBounds getViewport() {
                return null;
            }

            @Nullable
            @Override
            public Uri getWebsiteUri() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        };
        places = Arrays.asList(place0, place1, place2, place3);
    }
}
