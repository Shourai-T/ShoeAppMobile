package com.example.ban_giay_mobile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ban_giay_mobile.Model.BrandModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.databinding.ViewholderBrandBinding

class BrandAdapter(val items:MutableList<BrandModel>):RecyclerView.Adapter<BrandAdapter.ViewHolder>() {
    private  var selectedPosition = -1
    private var lastSelectedPosition = -1
    private  lateinit var context: Context
    class ViewHolder (val binding: ViewholderBrandBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderBrandBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.title.text = item.title
        Glide.with(holder.itemView.context).load(item.picUrl).into(holder.binding.pic)
        holder.binding.root.setOnClickListener {
            selectedPosition = position
            notifyItemChanged(selectedPosition)
            notifyItemChanged(lastSelectedPosition)
            lastSelectedPosition = selectedPosition
        }
        holder.binding.title.setTextColor(context.resources.getColor(R.color.white))
        if (selectedPosition == position) {
            holder.binding.pic.setBackgroundResource(0)
            holder.binding.mailLayout.setBackgroundColor(R.color.purple)
            ImageViewCompat.setImageTintList(holder.binding.pic, ColorStateList.valueOf(context.getColor(R.color.white)))
            holder.binding.title.visibility = View.VISIBLE
        } else {
            holder.binding.mailLayout.setBackgroundResource(R.color.grey)
            holder.binding.pic.setBackgroundColor(0)
            ImageViewCompat.setImageTintList(holder.binding.pic, ColorStateList.valueOf(context.getColor(R.color.black)))
            holder.binding.title.visibility = View.GONE
        }
    }

}