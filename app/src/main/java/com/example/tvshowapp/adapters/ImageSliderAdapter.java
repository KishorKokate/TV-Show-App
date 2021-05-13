package com.example.tvshowapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowapp.R;
import com.example.tvshowapp.databinding.ItemContainerSliderImageBinding;

public class ImageSliderAdapter  extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewholder> {

    private String[] sliderImages;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
       if (layoutInflater==null){
           layoutInflater=LayoutInflater.from(parent.getContext());
       }
       ItemContainerSliderImageBinding sliderImageBinding= DataBindingUtil.inflate(
               layoutInflater, R.layout.item_container_slider_image,parent,false
       );

        return new ImageSliderViewholder(sliderImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull  ImageSliderAdapter.ImageSliderViewholder holder, int position) {

        holder.bindSliderImage(sliderImages[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImages.length;
    }

    static class ImageSliderViewholder extends RecyclerView.ViewHolder{

        private ItemContainerSliderImageBinding itemContainerSliderImageBinding;


        public ImageSliderViewholder( ItemContainerSliderImageBinding itemContainerSliderImageBinding) {
            super(itemContainerSliderImageBinding.getRoot());
            this.itemContainerSliderImageBinding = itemContainerSliderImageBinding;
        }

        public void bindSliderImage(String imageURL){
            itemContainerSliderImageBinding.setImageURl(imageURL);
        }
    }

}
