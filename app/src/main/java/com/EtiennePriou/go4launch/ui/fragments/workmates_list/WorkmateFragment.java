package com.EtiennePriou.go4launch.ui.fragments.workmates_list;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseFragment;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.MainViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class WorkmateFragment extends BaseFragment {

    private static MainViewModel mMainViewModel;
    private List<Workmate> mWorkmates;

    public WorkmateFragment() { }


    public static WorkmateFragment newInstance(MainViewModel mainViewModel) {
        mMainViewModel = mainViewModel;
        return new WorkmateFragment();
    }

    @Override
    protected int setLayout() { return R.layout.fragment_workmate_list; }

    @Override
    protected void initList() {
        mWorkmates = new ArrayList<>();
        if (mFireBaseApi.getWorkmatesList() == null){
            UserHelper.getUserList().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    mFireBaseApi.setWorkmatesList(queryDocumentSnapshots.toObjects(Workmate.class));
                    observeSearch();
                    setAdapter(mFireBaseApi.getWorkmatesListNoMe());
                }
            });
        }else {
            observeSearch();
            setAdapter(mFireBaseApi.getWorkmatesListNoMe());
        }
    }

    private void observeSearch() {
        // Create the observer which updates the UI.
        final Observer<String> searchObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String search) {
                if (search != null || !search.isEmpty()){
                    for (Workmate workmate : mFireBaseApi.getWorkmatesListNoMe()){
                        if (workmate.getUsername().contains(search)){
                            mWorkmates.add(workmate);
                        }
                    }
                    if (!mWorkmates.isEmpty()) setAdapter(mWorkmates);
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mMainViewModel.getWorkmateSearch().observe(this, searchObserver);
    }

    private void setAdapter(List<Workmate> workmateList){ mRecyclerView.setAdapter(new MyWorkmateRecyclerViewAdapter(workmateList,1)); }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}