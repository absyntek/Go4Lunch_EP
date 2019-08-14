package com.EtiennePriou.go4launch.ui.fragments.place_view;

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
import com.EtiennePriou.go4launch.models.Place;
import com.EtiennePriou.go4launch.ui.DetailPlaceActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyPlaceRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaceRecyclerViewAdapter.ViewHolder> {

    private final List<Place> mValues;
    private Context mContext;
    private static final String PLACEREFERENCE = "placeReference";

    public MyPlaceRecyclerViewAdapter(List items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Place place = mValues.get(position);
        holder.mtvNamePlace.setText(place.getName());
        holder.mtvAdresse.setText(place.getAdresse());

        if (place.getImgReference() != null){
            Glide.with(holder.imgPlaceListe.getContext())
                    .load(place.getPhotoUri())
                    .into(holder.imgPlaceListe);
        }else {
            holder.imgPlaceListe.setImageResource(R.drawable.notext_logo200x200);
        }
        if (place.isOpen() == null){
            holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.quantum_black_text));
            holder.mtvIsOpen.setText(R.string.UnknownTime);
        }else{
            boolean isOpen = Boolean.parseBoolean(place.isOpen());
            if (isOpen){
                holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.green));
                holder.mtvIsOpen.setText(R.string.open);

            }else {
                holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                holder.mtvIsOpen.setText(R.string.close);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailPlaceActivity.class);
                intent.putExtra(PLACEREFERENCE, place.getReference());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mtvNamePlace, mtvAdresse, mtvIsOpen;
        private final ImageView imgPlaceListe;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mtvNamePlace = view.findViewById(R.id.tvNamePlace);
            mtvAdresse = view.findViewById(R.id.tvAdressePlace);
            mtvIsOpen = mView.findViewById(R.id.tvIsOpen);
            imgPlaceListe = mView.findViewById(R.id.imgPlace);
            mContext = view.getContext();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mtvAdresse.getText() + "'";
        }
    }
}
