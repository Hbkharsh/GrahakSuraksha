package com.example.grahaksuraksha.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.grahaksuraksha.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class ImageSliderAdapter extends
        SliderViewAdapter<ImageSliderAdapter.ImageSliderAdapterVH> {
    int[] images;

    public ImageSliderAdapter(int[] images){

        this.images = images;

    }

//    public void renewItems(List<SliderItem> sliderItems) {
//        this.mSliderItems = sliderItems;
//        notifyDataSetChanged();
//    }
//
//    public void deleteItem(int position) {
//        this.mSliderItems.remove(position);
//        notifyDataSetChanged();
//    }
//
//    public void addItem(SliderItem sliderItem) {
//        this.mSliderItems.add(sliderItem);
//        notifyDataSetChanged();
//    }

    @Override
    public ImageSliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_slider_item, null);
        return new ImageSliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(ImageSliderAdapterVH viewHolder, final int position) {
        viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return images.length;
    }

    class ImageSliderAdapterVH extends ViewHolder {

      ImageView imageView;

        public ImageSliderAdapterVH(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.slider_item_img);
        }
    }

}