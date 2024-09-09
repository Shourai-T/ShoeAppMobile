package com.example.ban_giay_mobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ban_giay_mobile.Model.FavouriteModel
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.databinding.ViewholderFavouriteBinding

class FavouriteAdapter(
    private var items: List<ItemModel>,
    private val onDeleteClick: (ItemModel) -> Unit
) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val binding = ViewholderFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<ItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class FavouriteViewHolder(private val binding: ViewholderFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemModel) {
            binding.titleTxt.text = item.title
            binding.feeEachItem.text = "$${item.price}"
            binding.totalEachItem.text = "$${item.price}" // Assuming total is the same as fee for now

            // Load image (assuming you use a library like Glide or Picasso for image loading)
            Glide.with(binding.pic.context)
                .load(item.picUrl)
                .placeholder(R.drawable.grey_bg)
                .into(binding.pic)

            binding.viewDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }
}
