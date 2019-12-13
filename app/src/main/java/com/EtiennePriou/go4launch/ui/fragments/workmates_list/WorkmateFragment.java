package com.EtiennePriou.go4launch.ui.fragments.workmates_list;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseFragment;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.MainViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class WorkmateFragment extends BaseFragment {

    private static MainViewModel mMainViewModel;
    private MyWorkmateRecyclerViewAdapter mAdapter;

    public WorkmateFragment() { }


    public static WorkmateFragment newInstance(MainViewModel mainViewModel) {
        mMainViewModel = mainViewModel;
        return new WorkmateFragment();
    }

    @Override
    protected int setLayout() { return R.layout.fragment_workmate_list; }

    @Override
    protected void initList() {
        observeSearch();
        setAdapter(UserHelper.getUsers());
    }

    private void observeSearch() {
        // Create the observer which updates the UI.
        final Observer<String> searchObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String search) {
                if (search != null || !search.isEmpty()){
                    setAdapter(UserHelper.getUsersSearch(search));
                    mAdapter.startListening();
                }else {
                    setAdapter(UserHelper.getUsers());
                    mAdapter.startListening();
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mMainViewModel.getWorkmateSearch().observe(this, searchObserver);
    }

    private void setAdapter(Query query){
        this.mAdapter = new MyWorkmateRecyclerViewAdapter(generateOptionsForAdapter(query),1);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });
        mRecyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Workmate> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions
                .Builder<Workmate>()
                .setQuery(query,Workmate.class)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}