package com.EtiennePriou.go4launch.ui.fragments.workmates_list;

import androidx.annotation.NonNull;

import android.util.Log;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseFragment;
import com.EtiennePriou.go4launch.models.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class WorkmateFragment extends BaseFragment {

    public WorkmateFragment() { }


    public static WorkmateFragment newInstance() {
        WorkmateFragment fragment = new WorkmateFragment();
        return fragment;
    }

    @Override
    protected int setLayout() { return R.layout.fragment_workmate_list; }

    @Override
    protected void initList() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Workmate> workmates = task.getResult().toObjects(Workmate.class);
                    mRecyclerView.setAdapter(new MyWorkmateRecyclerViewAdapter(workmates));

                }else {
                    Log.w("test", "Error getting documents.", task.getException());
                }
            }
        });

    }
}

