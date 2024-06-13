package com.example.sel.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter

class BindingUtils {
    companion object{
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImageWithGlide(imageView: ImageView, imageUrl: String?){
            Utils.loadImageWithGlide(imageView.context, imageUrl, imageView)
        }

    }
}