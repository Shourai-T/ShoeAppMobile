package com.example.ban_giay_mobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ban_giay_mobile.Model.ItemImagesModel
import com.example.ban_giay_mobile.databinding.ViewholderColorBinding

class ColorAdapter(private val colors: List<ItemImagesModel>) :
    RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderColorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colors[position]

        Glide.with(holder.itemView.context)
            .load(color.picUrl) // Tải URL hình ảnh
            .into(holder.binding.pic)
    }

    override fun getItemCount(): Int = colors.size
}
