package com.example.ban_giay_mobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ban_giay_mobile.Model.SizeModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.databinding.ViewholderSizeBinding

class SizeAdapter(
    private val sizeList: List<SizeModel>,
    private val onSizeSelected: (SizeModel) -> Unit
) :
    RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    private var selectedSize: SizeModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val binding = ViewholderSizeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size = sizeList[position]
        holder.bind(size)
    }

    override fun getItemCount(): Int = sizeList.size

    inner class SizeViewHolder(private val binding: ViewholderSizeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(size: SizeModel) {
            binding.sizeTxt.text = size.size // Assuming SizeModel has a property `sizeName`

            // Change background and text color if selected
            if (size == selectedSize) {
                binding.sizeLayout.setBackgroundResource(R.drawable.red_holder)
                binding.sizeTxt.setTextColor(binding.root.context.getColor(R.color.white))
            } else {
                binding.sizeLayout.setBackgroundResource(R.drawable.grey_bg)
                binding.sizeTxt.setTextColor(binding.root.context.getColor(R.color.black))
            }

            binding.root.setOnClickListener {
                selectedSize = size
                onSizeSelected(size)
                notifyDataSetChanged()
            }
        }
    }
}
