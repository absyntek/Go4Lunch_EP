package com.EtiennePriou.go4launch.ui;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public Fragment[] mFragments;

    public MainViewModel() {
        mFragments = new Fragment[] {null,null,null};
    }
}
