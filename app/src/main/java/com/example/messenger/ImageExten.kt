package com.example.messenger

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(url: String?, placeHolder: Int) {
    Glide
        .with(this.context)
        .load(url)
        .apply(RequestOptions.placeholderOf(placeHolder).error(placeHolder))
        .into(this)
}