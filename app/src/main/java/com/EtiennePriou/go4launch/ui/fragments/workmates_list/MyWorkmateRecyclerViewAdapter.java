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
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
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

        Glide.with(holder.workmateImage)
                .load(workmate.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.workmateImage);

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatTocken;

                if (workmate.getUid().compareTo(FirebaseAuth.getInstance().getUid()) > 0){
                    chatTocken = workmate.getUid()+ FirebaseAuth.getInstance().getUid();
                }else{
                    chatTocken = FirebaseAuth.getInstance().getUid() + workmate.getUid();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView workmateName;
        ImageView workmateImage;
        ImageView btnChat;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            workmateName =itemView.findViewById(R.id.tvWorkemate);
            workmateImage = itemView.findViewById(R.id.imgWorkmate);
            btnChat = itemView.findViewById(R.id.imgChat);
        }
    }
}
