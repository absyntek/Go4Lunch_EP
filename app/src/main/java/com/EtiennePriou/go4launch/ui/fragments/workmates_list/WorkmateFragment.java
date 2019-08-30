package com.EtiennePriou.go4launch.ui.fragments.workmates_list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseFragment;
import com.EtiennePriou.go4launch.models.Workmate;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


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
                    Query query = (Query) task.getResult().getDocuments();
                    populateAdapter(query);
                }else {
                    Log.w("test", "Error getting documents.", task.getException());
                }
            }
        });

    }

    private void populateAdapter (Query query){

        FirebaseRecyclerOptions<Workmate> options =
                new FirebaseRecyclerOptions.Builder<Workmate>()
                        .setQuery(query, Workmate.class)
                        .build();

        FirebaseRecyclerAdapter test = new FirebaseRecyclerAdapter<Workmate,WorkmateHolder>(options) {
            @NonNull
            @Override
            public WorkmateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_workmate,parent,false);
                return new WorkmateHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull WorkmateHolder workmateHolder, int i, @NonNull Workmate workmate) {
                workmateHolder.workmateName.setText(workmate.getUsername());
                Glide.with(getContext()).load(workmate.getUrlPicture()).into(workmateHolder.workmateImage);
            }

        };

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(test);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

    class WorkmateHolder extends RecyclerView.ViewHolder{

        public TextView workmateName;
        public ImageView workmateImage;
        public WorkmateHolder(View itemView) {
            super(itemView);
            workmateName =itemView.findViewById(R.id.tvWorkemate);
            workmateImage = itemView.findViewById(R.id.imgWorkmate);
        }
    }

