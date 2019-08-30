package com.EtiennePriou.go4launch.ui.fragments.place_view;

import android.content.Context;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.models.Places;
import com.EtiennePriou.go4launch.base.BaseFragment;

import java.util.List;

public class PlaceFragment extends BaseFragment {

    private static List<Places> mHashMaps;

    public PlaceFragment() { }

    public static PlaceFragment newInstance() {
        PlaceFragment fragment = new PlaceFragment();
        return fragment;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_place_list;
    }

    @Override
    protected void initList() {
        mHashMaps = mPlacesApiService.getNearbyPlacesList();
        if (mHashMaps != null) mRecyclerView.setAdapter(new MyPlaceRecyclerViewAdapter(mHashMaps));
    }
}
