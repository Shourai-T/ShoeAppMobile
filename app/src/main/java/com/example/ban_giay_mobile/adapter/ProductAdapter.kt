package com.example.ban_giay_mobile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.activity.DetailActivity
import com.example.ban_giay_mobile.databinding.ViewholderRecommendedBinding

class ProductAdapter(private val items: List<ItemModel>):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var context: Context? = null

    class ViewHolder(val binding: ViewholderRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            ViewholderRecommendedBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTxt.text=items[position].title
        holder.binding.priceTxt.text="$"+items[position].price.toString()
        holder.binding.ratingTxt.text=items[position].rating.toString()

        val imageUrl = item.picUrl
        Log.d("PopularAdapter", "Item data: $imageUrl")

        val requestOptions = RequestOptions().centerCrop()
        Glide.with(holder.itemView.context)
            .load(imageUrl) // Tải URL hình ảnh
            .apply(requestOptions)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            val context =holder.itemView.context
            // Tạo Intent để mở DetailActivity và truyền ID sản phẩm
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("ITEM_ID", item.id) // Truyền ID của sản phẩm
            }
            context?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size
}
