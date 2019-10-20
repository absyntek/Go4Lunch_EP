package com.EtiennePriou.go4launch.ui;

import com.EtiennePriou.go4launch.BuildConfig;
import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.di.ViewModelFactory;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.fragments.MapFragment;
import com.EtiennePriou.go4launch.ui.fragments.place_view.PlaceFragment;
import com.EtiennePriou.go4launch.ui.fragments.workmates_list.WorkmateFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainViewModel mMainViewModel;
    private static final String PLACEREFERENCE = "placeReference";
    private TextView mtv_Menu_Mail;
    private TextView mtv_Menu_Name;
    private ImageView imgMenuProfile;
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    if (mMainViewModel.mFragments[0] == null){
                        mMainViewModel.mFragments[0] = MapFragment.newInstance();
                        showFragment(mMainViewModel.mFragments[0]);
                    }else showFragment(mMainViewModel.mFragments[0]);
                    return true;
                case R.id.navigation_list_view:
                    if (mMainViewModel.mFragments[1] == null){
                        mMainViewModel.mFragments[1] = PlaceFragment.newInstance();
                        showFragment(mMainViewModel.mFragments[1]);
                    }else showFragment(mMainViewModel.mFragments[1]);
                    return true;
                case R.id.navigation_workmates:
                    if (mMainViewModel.mFragments[2] == null){
                        mMainViewModel.mFragments[2] = WorkmateFragment.newInstance();
                        showFragment(mMainViewModel.mFragments[2]);
                    }else showFragment(mMainViewModel.mFragments[2]);
                    return true;
            }
            return false;
        }
    };

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupUi(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        View headerView = navigationView.getHeaderView(0);
        imgMenuProfile = headerView.findViewById(R.id.img_menu_profile);
        mtv_Menu_Mail = headerView.findViewById(R.id.tv_menu_mail);
        mtv_Menu_Name = headerView.findViewById(R.id.tv_menu_name);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void withOnCreate() {
        configureViewModel();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        currentUser.getEmail();
        Places.initialize(this.getApplicationContext(), BuildConfig.PlaceApiKey);
        if (currentUser != null){
            mFireBaseApi.setCurrentUser(currentUser);
            setupMenuInfo(currentUser);
        }

        if (mFireBaseApi.getWorkmatesList() == null) mFireBaseApi.updateWorkmatesList();
        showFragment(MapFragment.newInstance());
    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = DI.provideViewModelFactory();
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    private void setupMenuInfo(FirebaseUser user){
        mtv_Menu_Mail.setText(user.getEmail());
        mtv_Menu_Name.setText(user.getDisplayName()); //TODO Return null WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY ???????????????????
        Glide.with(imgMenuProfile.getContext())
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imgMenuProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_your_lunch:
                UserHelper.getUser(mFireBaseApi.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.get("placeToGo") != null){//TODO faire quelque chose si null
                            Intent intent = new Intent(getApplicationContext(), DetailPlaceActivity.class);
                            intent.putExtra(PLACEREFERENCE, Objects.requireNonNull(documentSnapshot.get("placeToGo")).toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(intent);
                        }
                    }
                });
                break;
            case R.id.nav_setting:
                Intent intentSetting = new Intent(this,SettingsActivity.class);
                this.startActivity(intentSetting);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                this.finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
