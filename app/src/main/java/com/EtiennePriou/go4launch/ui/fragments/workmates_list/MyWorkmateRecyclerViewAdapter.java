package com.EtiennePriou.go4launch.ui.fragments.workmates_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.models.Workmate;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyWorkmateRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkmateRecyclerViewAdapter.ViewHolder>{

    private List<Workmate> mWorkmates;

    public MyWorkmateRecyclerViewAdapter(List<Workmate> workmates) { mWorkmates = workmates; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmate, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Workmate workmate = mWorkmates.get(position);
        holder.workmateName.setText(workmate.getUsername());
        Glide.with(holder.workmateImage).load(workmate.getUrlPicture()).into(holder.workmateImage);
    }

    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView workmateName;
        public ImageView workmateImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            workmateName =itemView.findViewById(R.id.tvWorkemate);
            workmateImage = itemView.findViewById(R.id.imgWorkmate);
        }
    }
}
