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
import android.widget.RatingBar;
import android.widget.TextView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.places.PlacesApi;
import com.EtiennePriou.go4launch.services.places.helpers.DetailHelper;
import com.EtiennePriou.go4launch.ui.MainViewModel;
import com.EtiennePriou.go4launch.ui.details.DetailPlaceActivity;
import com.EtiennePriou.go4launch.utils.CheckDate;
import com.EtiennePriou.go4launch.utils.CheckTime;
import com.EtiennePriou.go4launch.utils.DistanceBeetwin;
import com.EtiennePriou.go4launch.utils.NoteCalcul;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyPlaceRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaceRecyclerViewAdapter.ViewHolder> {

    private final List<Place> mPlaceModelList;
    private MainViewModel mMainViewModel;
    private PlacesApi mPlacesApi;
    private Context mContext;

    MyPlaceRecyclerViewAdapter(List<Place> items, MainViewModel mainViewModel) {
        this.mPlaceModelList = items;
        this.mMainViewModel = mainViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place, parent, false);
        mPlacesApi = DI.getServiceApiPlaces();
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Place placeModel = mPlaceModelList.get(position);
        holder.mtvNamePlace.setText(placeModel.getName());
        holder.mtvAdresse.setText(placeModel.getAddress());

        // -- load image --
        if (placeModel.getPhotoMetadatas() != null){
                DetailHelper.getPhoto(placeModel,mPlacesApi.getPlacesClient()).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                    @Override
                    public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                        Glide.with(holder.imgPlaceListe).load(fetchPhotoResponse.getBitmap()).into(holder.imgPlaceListe);
                    }
                });
        }

        // -- Distance between points --
        String s = DistanceBeetwin.calculDistance(placeModel.getLatLng(),mMainViewModel.getLocation());
        holder.mtvDistance.setText(s);

        // -- check opening time --
        DetailHelper.getHours(placeModel.getId(),mPlacesApi.getPlacesClient()).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                if (fetchPlaceResponse.getPlace().getOpeningHours() !=null){
                    Map toShow = CheckTime.getStringTime(fetchPlaceResponse.getPlace().getOpeningHours(), mContext);
                    String sToShow = String.valueOf(toShow.get("string"));
                    Boolean open = (Boolean) toShow.get("open");
                    holder.mtvIsOpen.setText(sToShow);
                    if (open){
                        holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext ,R.color.green));
                    }else {
                        holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                    }
                }else {
                    holder.mtvIsOpen.setText(mContext.getString(R.string.dont_know_open_time));
                }
            }
        });

        // -- Check number of workmates who coming to this place --
        PlaceHelper
                .getWhoComing(placeModel.getId())
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int tmp = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (!CheckDate.isDatePast(documentSnapshot.getDate("dateCreated"))){
                        tmp++;
                    }
                }
                holder.mtvNbrWorkmates.setText(String.valueOf(tmp));
            }
        });

        PlaceHelper.getNotes(placeModel.getId()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                holder.mRatingBar.setRating(NoteCalcul.calateNote(queryDocumentSnapshots));
            }
        });

        // -- set listener for opening detail activity --
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailPlaceActivity.class);
                intent.putExtra(view.getResources().getString(R.string.PLACEREFERENCE), placeModel.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaceModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mtvNamePlace, mtvAdresse, mtvIsOpen, mtvNbrWorkmates, mtvDistance;
        private final ImageView imgPlaceListe;
        private final RatingBar mRatingBar;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mtvNamePlace = view.findViewById(R.id.tvNamePlace);
            mtvAdresse = view.findViewById(R.id.tvAdressePlace);
            mtvIsOpen = view.findViewById(R.id.tvIsOpen);
            mtvNbrWorkmates = view.findViewById(R.id.tvWorkmateComming);
            mtvDistance = view.findViewById(R.id.tvDistance);
            imgPlaceListe = view.findViewById(R.id.imgPlace);
            mRatingBar = view.findViewById(R.id.ratingBarList);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mtvAdresse.getText() + "'";
        }
    }
}
