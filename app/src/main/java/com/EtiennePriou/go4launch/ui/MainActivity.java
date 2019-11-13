package com.EtiennePriou.go4launch.ui;

import com.EtiennePriou.go4launch.BuildConfig;
import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.di.ViewModelFactory;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.details.DetailPlaceActivity;
import com.EtiennePriou.go4launch.ui.fragments.MapFragment;
import com.EtiennePriou.go4launch.ui.fragments.place_view.PlaceFragment;
import com.EtiennePriou.go4launch.ui.fragments.workmates_list.WorkmateFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainViewModel mMainViewModel;
    private static final String PLACEREFERENCE = "placeReference";
    private int AUTOCOMPLETE_REQUEST_CODE = 1;
    private TextView mtv_Menu_Mail;
    private TextView mtv_Menu_Name;
    private ImageView imgMenuProfile;
    private Boolean search = true;
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    search = true;
                    if (mMainViewModel.getFragment(0) == null){
                        mMainViewModel.setFragment(0,MapFragment.newInstance(mMainViewModel));
                        showFragment(mMainViewModel.getFragment(0));
                    }else showFragment(mMainViewModel.getFragment(0));
                    return true;
                case R.id.navigation_list_view:
                    search = true;
                    if (mMainViewModel.getFragment(1) == null){
                        mMainViewModel.setFragment(1,PlaceFragment.newInstance(mMainViewModel));
                        showFragment(mMainViewModel.getFragment(1));
                    }else showFragment(mMainViewModel.getFragment(1));
                    return true;
                case R.id.navigation_workmates:
                    search = false;
                    if (mMainViewModel.getFragment(2) == null){
                        mMainViewModel.setFragment(2,WorkmateFragment.newInstance());
                        showFragment(mMainViewModel.getFragment(2));
                    }else showFragment(mMainViewModel.getFragment(2));
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
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        imgMenuProfile = headerView.findViewById(R.id.img_menu_profile);
        mtv_Menu_Mail = headerView.findViewById(R.id.tv_menu_mail);
        mtv_Menu_Name = headerView.findViewById(R.id.tv_menu_name);
    }

    @Override
    protected void withOnCreate() {
        configureViewModel();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();
        Places.initialize(this.getApplicationContext(), BuildConfig.PlaceApiKey);
        if (currentUser != null){
            mFireBaseApi.setCurrentUser(currentUser);
            UserHelper.getUser(currentUser.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    mFireBaseApi.setActualUser(documentSnapshot.toObject(Workmate.class));
                }
            });
            setupMenuInfo(currentUser);
        }

        if (mFireBaseApi.getWorkmatesList() == null){
            UserHelper.getUserList().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    mFireBaseApi.setWorkmatesList(queryDocumentSnapshots.toObjects(Workmate.class));
                    mFireBaseApi.setWorkmateListNoMe(currentUser.getUid());
                }
            });

        }
        showFragment(MapFragment.newInstance(mMainViewModel));
    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = DI.provideViewModelFactory();
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    private void setupMenuInfo(FirebaseUser user){
        mtv_Menu_Mail.setText(user.getEmail());
        mtv_Menu_Name.setText(user.getDisplayName()); //TODO Return null change on firebase email verif
        Glide.with(imgMenuProfile.getContext())
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imgMenuProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (search){
            inflater.inflate(R.menu.options_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search){ setSearch(); }
        return true;
    }

    private void setSearch() {
        if (search){
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.TYPES);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setTypeFilter(TypeFilter.REGIONS)
                    .setCountry("FR")
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }else{
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE){
            if (requestCode == RESULT_OK){
                Place place = Autocomplete.getPlaceFromIntent(data);
            }else if (requestCode == AutocompleteActivity.RESULT_ERROR){
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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
                        Workmate user = documentSnapshot.toObject(Workmate.class);
                        if (user.getPlaceToGo() != null){//TODO faire quelque chose si null
                            Intent intent = new Intent(getApplicationContext(), DetailPlaceActivity.class);
                            intent.putExtra(PLACEREFERENCE, Objects.requireNonNull(user.getPlaceToGo().get("placeRef").toString()));
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
