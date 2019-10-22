package com.EtiennePriou.go4launch.ui.fragments.workmates_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.ui.chat.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class MyWorkmateRecyclerViewAdapter extends RecyclerView.Adapter<MyWorkmateRecyclerViewAdapter.ViewHolder>{

    private List<Workmate> mWorkmates;
    private int wichActi;

    public MyWorkmateRecyclerViewAdapter(List<Workmate> workmates, int wichActi) {
        this.mWorkmates = workmates;
        this.wichActi = wichActi;
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

        final Workmate workmate = mWorkmates.get(position);

        if (wichActi == 0){
            String isJoin = workmate.getUsername()+" is joining!";
            holder.workmateName.setText(isJoin);
        }else{
            String whereIsGoing;
            if (workmate.getPlaceToGo() != null){
                whereIsGoing = workmate.getUsername() + " is going to " + workmate.getPlaceToGo().get("placeName").toString();
            }else {
                whereIsGoing = workmate.getUsername() + " didn't choose yet";
            }
            holder.workmateName.setText(whereIsGoing);

        }


        Glide.with(holder.workmateImage)
                .load(workmate.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.workmateImage);

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatToken;

                if (workmate.getUid().compareTo(FirebaseAuth.getInstance().getUid()) > 0){
                    chatToken = workmate.getUid()+ FirebaseAuth.getInstance().getUid();
                }else{
                    chatToken = FirebaseAuth.getInstance().getUid() + workmate.getUid();
                }
                Intent chat = new Intent(view.getContext(), ChatActivity.class);
                chat.putExtra("token",chatToken);
                view.getContext().startActivity(chat);
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
