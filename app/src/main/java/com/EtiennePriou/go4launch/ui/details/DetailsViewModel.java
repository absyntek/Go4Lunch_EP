package com.EtiennePriou.go4launch.ui.details;

import androidx.lifecycle.ViewModel;

import com.EtiennePriou.go4launch.models.Workmate;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    private List<Workmate> mWorkmatesThisPlace = null;

    List<Workmate> getWorkmatesThisPlace() {
        return mWorkmatesThisPlace;
    }

    void setWorkmatesThisPlace(List<Workmate> workmatesThisPlace) {
        mWorkmatesThisPlace = workmatesThisPlace;
    }
}
