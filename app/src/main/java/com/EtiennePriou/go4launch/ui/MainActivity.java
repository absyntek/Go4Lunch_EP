package com.EtiennePriou.go4launch.ui;

import android.os.Bundle;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.ui.fragments.MapFragment;
import com.EtiennePriou.go4launch.ui.fragments.place_view.PlaceFragment;
import com.EtiennePriou.go4launch.ui.fragments.workmates_list.WorkmateFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mtv_Menu_Mail;
    private TextView mtv_Menu_Name;
    private ImageView imgMenuProfile;
    private FrameLayout mFrameLayout;
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    showFragment(new MapFragment());
                    return true;
                case R.id.navigation_list_view:
                    showFragment(new PlaceFragment());
                    return true;
                case R.id.navigation_workmates:
                    showFragment(new WorkmateFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null)setupMenuInfo(currentUser);

    }

    private void setupUi (){

        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        mFrameLayout = findViewById(R.id.container);

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
    private void setupMenuInfo(FirebaseUser user){
        mtv_Menu_Mail.setText(user.getEmail());
        mtv_Menu_Name.setText(user.getDisplayName());
        Glide.with(imgMenuProfile.getContext())
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imgMenuProfile);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_your_lunch) {
            //TODO implement fragment map
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            this.finish();
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
