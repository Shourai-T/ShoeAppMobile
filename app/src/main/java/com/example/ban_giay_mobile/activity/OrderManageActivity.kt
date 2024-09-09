package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.OrderManageAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityOrdersManageBinding
import com.example.ban_giay_mobile.repository.*

class OrderManageActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityOrdersManageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo các repository
        val bannerRepository = BannerRepository(RetrofitClient.instance)
        val brandRepository = BrandRepository(RetrofitClient.instance)
        val itemRepository = ItemRepository(RetrofitClient.instance)
        val favouriteRepository = FavouriteRepository(RetrofitClient.instance)
        val userRepository = UserRepository(RetrofitClient.instance)
        val orderRepository = OrderRepository(RetrofitClient.instance)

        // Sử dụng MainViewModelFactory để tạo MainViewModel
        val viewModelFactory = MainViewModelFactory(bannerRepository, brandRepository, itemRepository, favouriteRepository, userRepository, orderRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        initOrders()

        // Lắng nghe kết quả xóa đơn hàng
        viewModel.deleteOrderResult.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Toast.makeText(this, "Xóa đơn hàng thành công", Toast.LENGTH_SHORT).show()
                initOrders() // Tải lại danh sách đơn hàng sau khi xóa
            } else {
                Toast.makeText(this, "Xóa đơn hàng thất bại", Toast.LENGTH_SHORT).show()
            }
        })

        // Lắng nghe lỗi
        viewModel.error.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun initOrders() {
        binding.progressBarOrder.visibility = View.VISIBLE

        // Quan sát dữ liệu từ ViewModel
        viewModel.orders.observe(this, Observer { orders ->
            if (orders.isNotEmpty()) {
                binding.orderRecyclerView.layoutManager = LinearLayoutManager(this@OrderManageActivity)
                binding.orderRecyclerView.adapter = OrderManageAdapter(orders) { orderId ->
                    viewModel.deleteOrder(orderId)  // Gọi hàm xóa đơn hàng từ ViewModel
                }
            } else {
                Toast.makeText(this, "Không có đơn hàng nào", Toast.LENGTH_SHORT).show()
            }
            binding.progressBarOrder.visibility = View.GONE
        })

        // Gọi hàm lấy danh sách đơn hàng từ ViewModel
        viewModel.getAllOrders()
    }

}
