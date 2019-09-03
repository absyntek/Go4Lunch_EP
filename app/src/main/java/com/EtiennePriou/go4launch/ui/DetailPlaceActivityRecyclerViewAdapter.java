package com.EtiennePriou.go4launch.ui;

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
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

public class DetailPlaceActivityRecyclerViewAdapter extends RecyclerView.Adapter<DetailPlaceActivityRecyclerViewAdapter.ViewHolder>{

    private List<Workmate> mWorkmatesThisPlace;

    DetailPlaceActivityRecyclerViewAdapter(List<Workmate> workmatesThisPlace) {
        this.mWorkmatesThisPlace = workmatesThisPlace;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmate, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Workmate workmate = mWorkmatesThisPlace.get(position);

        holder.workmateName.setText(workmate.getUsername());

        Glide.with(holder.workmateImage)
                .load(workmate.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.workmateImage);
    }

    @Override
    public int getItemCount() {
        return mWorkmatesThisPlace.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView workmateName;
        ImageView workmateImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            workmateName =itemView.findViewById(R.id.tvWorkemate);
            workmateImage = itemView.findViewById(R.id.imgWorkmate);
        }
    }
}
