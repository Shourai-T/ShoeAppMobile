package com.example.ban_giay_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ban_giay_mobile.Model.OrderDetailModel
import com.example.ban_giay_mobile.R

class OrderDetailAdapter(private val orderDetails: List<OrderDetailModel>) : RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.textView31)
        val quantity: TextView = itemView.findViewById(R.id.textView33)
        val size: TextView = itemView.findViewById(R.id.textView34)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_order_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = orderDetails[position]
        holder.productName.text = detail.productName
        holder.quantity.text = detail.quantity.toString()
        holder.size.text = detail.size
    }

    override fun getItemCount(): Int = orderDetails.size
}
