package com.EtiennePriou.go4launch.ui.fragments.workmates_list;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseFragment;
import com.EtiennePriou.go4launch.events.ReceiveWorkmatePlace;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class WorkmateFragment extends BaseFragment {

    public WorkmateFragment() { }


    public static WorkmateFragment newInstance() {
        return new WorkmateFragment();
    }

    @Override
    protected int setLayout() { return R.layout.fragment_workmate_list; }

    @Override
    protected void initList() {
        if (mFireBaseApi.getWorkmatesList() != null) setAdapter();
    }

    private void setAdapter(){
        mRecyclerView.setAdapter(new MyWorkmateRecyclerViewAdapter(mFireBaseApi.getWorkmatesList()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onReceiveList(ReceiveWorkmatePlace event){
        setAdapter();
    }
}