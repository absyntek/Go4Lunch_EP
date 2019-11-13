package com.EtiennePriou.go4launch.ui.fragments.place_view;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseFragment;
import com.EtiennePriou.go4launch.ui.MainViewModel;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

public class PlaceFragment extends BaseFragment {

    private static MainViewModel mMainViewModel;


    public PlaceFragment() { }

    public static PlaceFragment newInstance(MainViewModel mainViewModel) {
        mMainViewModel = mainViewModel;
        return new PlaceFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_place_list;
    }

    @Override
    protected void initList() {
        List<Place> nearbyPlaceModelList = mPlacesApi.getPlaces();
        if (nearbyPlaceModelList != null) mRecyclerView.setAdapter(new MyPlaceRecyclerViewAdapter(nearbyPlaceModelList,mMainViewModel));
    }
}
