package com.example.ban_giay_mobile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.databinding.ItemSliderImageBinding

class DetailAdapter(private val images: List<String>) :
    RecyclerView.Adapter<DetailAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(val binding: ItemSliderImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding =
            ItemSliderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val imageUrl = images[position]
        Log.d("DetailAdapter", "Loading image: $imageUrl") // Log picUrl
        val requestOptions = RequestOptions().centerCrop()
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int = images.size
}
