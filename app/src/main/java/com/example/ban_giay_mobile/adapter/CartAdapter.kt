package com.example.ban_giay_mobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ban_giay_mobile.Helper.ChangeNumberItemsListener
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.databinding.ViewholderCartBinding

class CartAdapter(
    private val itemList: ArrayList<ItemModel>,
    private val context: Context,
    private val changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.plusCartBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList[position]
                    item.quantity += 1
                    notifyItemChanged(position)
                    changeNumberItemsListener?.onItemCountChanged(position, item.quantity)
                }
            }

            binding.minusCartBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList[position]
                    if (item.quantity > 0) {
                        item.quantity -= 1
                        notifyItemChanged(position)
                        changeNumberItemsListener?.onItemCountChanged(position, item.quantity)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "$${item.price}"
        holder.binding.sizeTxt.text = "Size ${item.size}"
        holder.binding.totalEachItem.text = "$${item.price * item.quantity}"

        Glide.with(context)
            .load(item.picUrl) // Load ảnh từ picUrl
            .placeholder(R.drawable.shoes)
            .into(holder.binding.pic)

        holder.binding.numberItemTxt.text = item.quantity.toString()
    }

    override fun getItemCount(): Int = itemList.size
}
