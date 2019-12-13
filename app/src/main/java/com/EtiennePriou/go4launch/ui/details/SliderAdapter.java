package com.EtiennePriou.go4launch.ui.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.services.places.PlacesApi;
import com.EtiennePriou.go4launch.services.places.helpers.DetailHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

        private List<PhotoMetadata> mPhotoMetadata;
        private PlacesApi mPlacesApi;

        SliderAdapter(List<PhotoMetadata> photoMetadata) {
            this.mPhotoMetadata = photoMetadata;
            mPlacesApi = DI.getServiceApiPlaces();
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(final SliderAdapterVH viewHolder, int position) {
            DetailHelper.getPhotoForDetail(mPhotoMetadata.get(position), mPlacesApi.getPlacesClient()).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                @Override
                public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                    Glide.with(viewHolder.itemView).load(fetchPhotoResponse.getBitmap()).centerCrop().into(viewHolder.imageViewBackground);
                }
            });
        }

        @Override
        public int getCount() {
            return mPhotoMetadata.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;
            TextView textViewDescription;

            SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
                this.itemView = itemView;
            }
        }
}
