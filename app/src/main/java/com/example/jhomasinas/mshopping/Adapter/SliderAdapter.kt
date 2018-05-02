package com.example.jhomasinas.mshopping.Adapter


import com.example.jhomasinas.mshopping.R
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

/**
 * Created by JhomAsinas on 4/27/2018.
 */
class SliderAdapter : SliderAdapter() {
    override fun getItemCount(): Int {
        return 4;
    }

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        when(position){
            0 -> imageSlideViewHolder?.bindImageSlide(R.drawable.sale4)
            1 -> imageSlideViewHolder?.bindImageSlide(R.drawable.sale1)
            2 -> imageSlideViewHolder?.bindImageSlide(R.drawable.sale2)
            3 -> imageSlideViewHolder?.bindImageSlide(R.drawable.sale3)
        }
    }
}