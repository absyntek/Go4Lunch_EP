package com.EtiennePriou.go4launch.ui.fragments.place_view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.EtiennePriou.go4launch.utils.DetailHelper;
import com.EtiennePriou.go4launch.ui.MainViewModel;
import com.EtiennePriou.go4launch.ui.details.DetailPlaceActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyPlaceRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaceRecyclerViewAdapter.ViewHolder> {

    private final List<Place> mPlaceModelList;
    private Context mContext;
    private MainViewModel mMainViewModel;
    private static final String PLACEREFERENCE = "placeReference";
    private PlacesApi mPlacesApi;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Place placeModel = mPlaceModelList.get(position);
        holder.mtvNamePlace.setText(placeModel.getName());
        holder.mtvAdresse.setText(placeModel.getAddress());

        // -- load image --
        PhotoMetadata photoMetadata = placeModel.getPhotoMetadatas().get(0);
        String attributions = photoMetadata.getAttributions();
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();

        DetailHelper.getPhoto(placeModel,mPlacesApi.getPlacesClient()).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
            @Override
            public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                holder.imgPlaceListe.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imgPlaceListe.setImageResource(R.drawable.notext_logo200x200);
                if (e instanceof ApiException){
                    ApiException apiException = (ApiException) e;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e("test", "Place not found: " + e.getMessage());
                }
            }
        });

        // -- Distance between points --
        holder.mtvDistance.setText(mMainViewModel.getDistanceBetween(placeModel.getLatLng()));

//        // -- check opening time --
//        if (placeModel.isOpen() == null){
//            holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.quantum_black_text));
//            holder.mtvIsOpen.setText(R.string.UnknownTime);
//        }else{
//            boolean isOpen = Boolean.parseBoolean(placeModel.isOpen());
//            if (isOpen){
//                holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//                holder.mtvIsOpen.setText(R.string.open);
//
//            }else {
//                holder.mtvIsOpen.setTextColor(ContextCompat.getColor(mContext,R.color.red));
//                holder.mtvIsOpen.setText(R.string.close);
//            }
//        }

        // -- Check number of workmates who coming to this place --
        PlaceHelper
                .getWhoComing(placeModel.getId())
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int nmbWork = queryDocumentSnapshots.size();
                holder.mtvNbrWorkmates.setText(String.valueOf(nmbWork));
            }
        });

        PlaceHelper.getNotes(placeModel.getId()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int placeNote = 0;
                if (queryDocumentSnapshots != null || queryDocumentSnapshots.size() > 0){
                    int divisor = queryDocumentSnapshots.size();
                    int noteToDivise = 0;
                    for (DocumentSnapshot note : queryDocumentSnapshots){
                        int noteTmp = note.get("note",Integer.class);
                        noteToDivise += noteTmp;
                    }
                    if (noteToDivise < 1){
                        placeNote = 0;
                    }else{
                        placeNote = Math.round(noteToDivise/divisor);
                    }
                    holder.mRatingBar.setRating(placeNote);
                }
            }
        });

        // -- set listener for opening detail activity --
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailPlaceActivity.class);
                intent.putExtra(PLACEREFERENCE, placeModel.getId());
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
            mContext = view.getContext();
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mtvAdresse.getText() + "'";
        }
    }
}
