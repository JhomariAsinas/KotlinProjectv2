package com.example.jhomasinas.mshopping.Config

import android.widget.ImageView
import com.squareup.picasso.Picasso
import ss.com.bannerslider.ImageLoadingService

/**
 * Created by JhomAsinas on 4/27/2018.
 */
class PicassoImageLoadingService : ImageLoadingService {

    override fun loadImage(url: String?, imageView: ImageView?) {
       Picasso.get().load(url).resize(350,250).into(imageView)
    }

    override fun loadImage(resource: Int, imageView: ImageView?) {
       Picasso.get().load(resource).resize(350,250).into(imageView)
    }

    override fun loadImage(url: String?, placeHolder: Int, errorDrawable: Int, imageView: ImageView?) {
        Picasso.get().load(url).resize(350,250).placeholder(placeHolder).error(errorDrawable).into(imageView)
    }
}