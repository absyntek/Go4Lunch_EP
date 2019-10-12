package com.EtiennePriou.go4launch.ui.fragments.place_view;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.ui.DetailPlaceActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyPlaceRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaceRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceModel> mPlaceModelList;
    private Context mContext;
    private static final String PLACEREFERENCE = "placeReference";

    MyPlaceRecyclerViewAdapter(List<PlaceModel> items) {
        mPlaceModelList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PlaceModel placeModel = mPlaceModelList.get(position);
        holder.mtvNamePlace.setText(placeModel.getName());
        holder.mtvAdresse.setText(placeModel.getAdresse());

        // -- load image --
        if (placeModel.getImgReference() != null){
            Glide.with(holder.imgPlaceListe.getContext())
                    .load(placeModel.getPhotoUri())
                    .into(holder.imgPlaceListe);
        }else {
            holder.imgPlaceListe.setImageResource(R.drawable.notext_logo200x200);
        }

        // -- check opening time --
        if (placeModel.isOpen() == null){
            holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.quantum_black_text));
            holder.mtvIsOpen.setText(R.string.UnknownTime);
        }else{
            boolean isOpen = Boolean.parseBoolean(placeModel.isOpen());
            if (isOpen){
                holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.green));
                holder.mtvIsOpen.setText(R.string.open);

            }else {
                holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                holder.mtvIsOpen.setText(R.string.close);
            }
        }

        // -- Check number of workmates who coming to this place --
        PlaceHelper
                .getWhoComing(placeModel.getReference())
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int nmbWork = queryDocumentSnapshots.size();
                holder.mtvNbrWorkmates.setText(String.valueOf(nmbWork));
            }
        });

        // -- set listener for opening detail activity --
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailPlaceActivity.class);
                intent.putExtra(PLACEREFERENCE, placeModel.getReference());
                view.getContext().startActivity(intent);
            }
        });
    }

    private void checkWhoComing (String placeRef){
        PlaceHelper.getWhoComing(placeRef).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaceModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mtvNamePlace, mtvAdresse, mtvIsOpen, mtvNbrWorkmates;
        private final ImageView imgPlaceListe;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mtvNamePlace = view.findViewById(R.id.tvNamePlace);
            mtvAdresse = view.findViewById(R.id.tvAdressePlace);
            mtvIsOpen = mView.findViewById(R.id.tvIsOpen);
            mtvNbrWorkmates = mView.findViewById(R.id.tvWorkmateComming);
            imgPlaceListe = mView.findViewById(R.id.imgPlace);
            mContext = view.getContext();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mtvAdresse.getText() + "'";
        }
    }
}
