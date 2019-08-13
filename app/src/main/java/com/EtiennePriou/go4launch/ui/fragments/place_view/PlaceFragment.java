package com.EtiennePriou.go4launch.ui.fragments.place_view;

import android.content.Context;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.ui.fragments.BaseFragment;

import java.util.HashMap;
import java.util.List;

public class PlaceFragment extends BaseFragment {

    private static List<HashMap<String, String>> mHashMaps;

    public PlaceFragment() {
    }

    public static PlaceFragment newInstance(List<HashMap<String, String>> hashMaps) {
        mHashMaps = hashMaps;
        PlaceFragment fragment = new PlaceFragment();
        return fragment;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_place_list;
    }

    @Override
    protected void initList() {
        if (mHashMaps != null) mRecyclerView.setAdapter(new MyPlaceRecyclerViewAdapter(mHashMaps));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
