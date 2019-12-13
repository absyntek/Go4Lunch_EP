package com.EtiennePriou.go4launch;

import android.location.Location;
import android.net.Uri;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.PlaceToGo;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.ui.MainActivity;
import com.EtiennePriou.go4launch.ui.fragments.place_view.PlaceFragment;
import com.EtiennePriou.go4launch.utils.DistanceBeetwin;
import com.google.android.gms.internal.firebase_auth.zzff;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.zzy;
import com.google.firebase.auth.zzz;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

public class MainActivityLargeTest {

    FirebaseUser mFirebaseUser;
    FireBaseApi mFireBaseApi;
    List<Place> mPlaces;
    List<Workmate> mWorkmateList;
    PlaceToGo mPlaceToGo;
    BottomNavigationView bottomNavigationView;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);
    @Before
    public void setUp (){
        FirebaseAuth.getInstance().signOut();
        bottomNavigationView = mActivityRule.getActivity().findViewById(R.id.bottom_nav_view);
        setPlaces();
        setWorkmates();
        mFireBaseApi = DI.getServiceFireBase();
        mFireBaseApi.setCurrentUser(mFirebaseUser);
        mFireBaseApi.setActualUser(mWorkmateList.get(0));
    }

    @Test
    public void testMainActi (){
        NavigationView navigationView = mActivityRule.getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView mtv_Menu_Mail = headerView.findViewById(R.id.tv_menu_mail);
        TextView mtv_Menu_Name = headerView.findViewById(R.id.tv_menu_name);

        assert mtv_Menu_Name.getText().equals(mWorkmateList.get(0).getUsername());
        assert mtv_Menu_Mail.getText().equals(mFirebaseUser.getEmail());
    }

    @Test
    public void calculDistance() {
        LatLng latLng = new LatLng(47.460046,-1.897052);
        Location location = new Location("locationA");
        location.setLatitude(47.457453);
        location.setLongitude(-1.923383);
        String s = DistanceBeetwin.calculDistance(latLng,location);
        assert s.equals("2Km");
    }

    private void setWorkmates() {
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
        mPlaceToGo = new PlaceToGo("000","000","res000");
        Workmate workmate0 = new Workmate("000","000",null,null);
        Workmate workmate1 = new Workmate("111","111",null,null);
        Workmate workmate2 = new Workmate("222","222",null,mPlaceToGo);
        Workmate workmate3 = new Workmate("333","333",null,mPlaceToGo);
        mWorkmateList =Arrays.asList(workmate0,workmate1,workmate2,workmate3);
    }

    private void setPlaces (){
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
        mPlaces = Arrays.asList(place0,place1,place2,place3);
    }
}
