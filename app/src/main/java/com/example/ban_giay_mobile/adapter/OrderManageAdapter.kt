package com.example.ban_giay_mobile.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ban_giay_mobile.Model.OrderModel
import com.example.ban_giay_mobile.activity.OrderDetailManageActivity
import com.example.ban_giay_mobile.databinding.ViewholderOrderBinding

class OrderManageAdapter(private val orders: List<OrderModel>, private val deleteOrder: (Int) -> Unit) :
    RecyclerView.Adapter<OrderManageAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder(val binding: ViewholderOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            ViewholderOrderBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]

        holder.binding.orderIdTxt.text = order.orderId.toString()
        holder.binding.statusTxt.text = order.status
        holder.binding.viewName.text = order.name
        holder.binding.viewAddress.text = order.address
        holder.binding.totalPriceTxt.text = "$${order.totalPrice}"
        holder.binding.viewPhone.text = order.phone

        // Xử lý click vào item để chuyển sang activity chi tiết
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, OrderDetailManageActivity::class.java).apply {
                putExtra("ORDER_ID", order.orderId) // Truyền Order ID để lấy chi tiết đơn hàng
            }
            context.startActivity(intent)
        }

        // Nút Delete: Xóa đơn hàng
        holder.binding.deleteBtn.setOnClickListener {
            // Hiển thị hộp thoại xác nhận xóa
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Xóa đơn hàng")
                .setMessage("Bạn có chắc muốn xóa đơn hàng này không?")
                .setPositiveButton("Xóa") { dialog, _ ->
                    // Gọi hàm xóa đơn hàng khi xác nhận
                    deleteOrder(order.orderId)
                    dialog.dismiss()
                }
                .setNegativeButton("Hủy") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }



    override fun getItemCount(): Int = orders.size
}
