package com.EtiennePriou.go4launch.ui.fragments.place_view;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.base.BaseFragment;

import java.util.List;

public class PlaceFragment extends BaseFragment {

    public PlaceFragment() { }

    public static PlaceFragment newInstance() {
        return new PlaceFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_place_list;
    }

    @Override
    protected void initList() {
        List<PlaceModel> nearbyPlaceModelList = mPlacesApi.getNearbyPlaceModelList();
        if (nearbyPlaceModelList != null) mRecyclerView.setAdapter(new MyPlaceRecyclerViewAdapter(nearbyPlaceModelList));
    }
}
