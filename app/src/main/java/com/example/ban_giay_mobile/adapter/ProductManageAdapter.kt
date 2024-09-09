package com.example.ban_giay_mobile.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.activity.DetailActivity
import com.example.ban_giay_mobile.activity.EditProductActivity
import com.example.ban_giay_mobile.databinding.ViewholderProductBinding

class ProductManageAdapter(private val items: List<ItemModel>, private val onItemSelected: (ItemModel) -> Unit) :
    RecyclerView.Adapter<ProductManageAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder(val binding: ViewholderProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "$${item.price}"
        holder.binding.ratingTxt.text = item.rating.toString()

        val imageUrl = item.picUrl
        Log.d("ProductManageAdapter", "Item data: $imageUrl")

        val requestOptions = RequestOptions().centerCrop()
        Glide.with(holder.itemView.context)
            .load(imageUrl) // Load image URL
            .apply(requestOptions)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            onItemSelected(item)
        }

    }

    override fun getItemCount(): Int = items.size
}
