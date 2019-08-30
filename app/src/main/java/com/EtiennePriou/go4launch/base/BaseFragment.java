package com.EtiennePriou.go4launch.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.services.firebase.UserHelper;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;

public abstract class BaseFragment extends Fragment {

    protected PlacesApiService mPlacesApiService;
    protected UserHelper mUserHelper;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager linearLayoutManager;

    protected abstract int setLayout();
    protected abstract void initList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlacesApiService = DI.getServiceApiPlaces();
        mUserHelper = new UserHelper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayout(),container,false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {
        return super.onGetLayoutInflater(savedInstanceState);
    }

    protected void refreshView() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
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
