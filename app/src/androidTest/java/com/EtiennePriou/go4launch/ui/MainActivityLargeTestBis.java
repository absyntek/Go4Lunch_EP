package com.EtiennePriou.go4launch.ui;

import android.location.Location;
import android.view.View;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.utils.DistanceBeetwin;
import com.EtiennePriou.go4launch.utils.PoppulateAPis;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class MainActivityLargeTestBis {

    private FireBaseApi mFireBaseApi = DI.getServiceFireBase();

    private List<Workmate> mWorkmateList;
    private FirebaseUser mFirebaseUser;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Before
    public void setUp (){

        FirebaseAuth.getInstance().signOut();
        PoppulateAPis.popApi();

        mWorkmateList = mFireBaseApi.getWorkmatesList();
        mFirebaseUser = mFireBaseApi.getCurrentUser();
        BottomNavigationView bottomNavigationView = mActivityRule.getActivity().findViewById(R.id.bottom_nav_view);
    }

    @Test
    public void testMainActi (){
        FirebaseAuth.getInstance().signOut();
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
}
