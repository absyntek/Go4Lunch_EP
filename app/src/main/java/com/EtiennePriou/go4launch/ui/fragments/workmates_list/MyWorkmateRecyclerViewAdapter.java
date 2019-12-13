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
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.ui.chat.ChatActivity;
import com.EtiennePriou.go4launch.ui.details.DetailPlaceActivity;
import com.EtiennePriou.go4launch.utils.CheckDate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.DetectedActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class MyWorkmateRecyclerViewAdapter extends FirestoreRecyclerAdapter<Workmate, MyWorkmateRecyclerViewAdapter.ViewHolder> {

    private int wichActivity;
    private FireBaseApi mFireBaseApi;

    public MyWorkmateRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options, int wichActi) {
        super(options);
        this.wichActivity = wichActi;
        mFireBaseApi = DI.getServiceFireBase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmate, parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Workmate model) {

        final Workmate workmate = model;

        if (wichActivity == 0)
        {
            if (workmate.getUid().equals(mFireBaseApi.getCurrentUser().getUid()))
            {
                holder.btnChat.setVisibility(View.GONE);
            }
            String isJoin = workmate.getUsername()+ holder.mContext.getString(R.string.isJoining);
            holder.workmateName.setText(isJoin);
        }else {
            if (workmate.getUid().equals(mFireBaseApi.getCurrentUser().getUid()))
            {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
            String whereIsGoing;
            if (workmate.getPlaceToGo() != null && !CheckDate.isDatePast(workmate.getPlaceToGo().getDateCreated()))
            {
                whereIsGoing = workmate.getUsername() + holder.mContext.getString(R.string.isGoingTo) + workmate.getPlaceToGo().getPlaceName();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent details = new Intent(view.getContext(), DetailPlaceActivity.class);
                        details.putExtra(view.getResources().getString(R.string.PLACEREFERENCE), Objects.requireNonNull(workmate.getPlaceToGo().getPlaceRef()));
                        view.getContext().startActivity(details);
                    }
                });
            }else {
                whereIsGoing = workmate.getUsername() + holder.mContext.getString(R.string.didntChoose);
            }
            holder.workmateName.setText(whereIsGoing);

        }

        if (workmate.getUrlPicture() != null)
        {
            Glide.with(holder.workmateImage)
                    .load(workmate.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.workmateImage);
        }


        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatToken;

                if (workmate.getUid().compareTo(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())) > 0){
                    chatToken = workmate.getUid()+ FirebaseAuth.getInstance().getUid();
                }else{
                    chatToken = FirebaseAuth.getInstance().getUid() + workmate.getUid();
                }
                Intent chat = new Intent(view.getContext(), ChatActivity.class);
                chat.putExtra(view.getResources().getString(R.string.TOKEN), chatToken);
                chat.putExtra(view.getResources().getString(R.string.RECEIVER), workmate.getUsername());
                view.getContext().startActivity(chat);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView workmateName;
        ImageView workmateImage;
        ImageView btnChat;
        View view;

        Context mContext;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            workmateName =itemView.findViewById(R.id.tvWorkemate);
            workmateImage = itemView.findViewById(R.id.imgWorkmate);
            btnChat = itemView.findViewById(R.id.imgChat);
        }
    }
}
