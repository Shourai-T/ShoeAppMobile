package com.example.ban_giay_mobile.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ban_giay_mobile.Model.OrderDetailModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.OrderDetailAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityOrderDetailManageBinding
import com.example.ban_giay_mobile.databinding.ActivityProductManageBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class OrderDetailManageActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityOrderDetailManageBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private val orderDetailList = mutableListOf<OrderDetailModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        // Cấu hình RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        orderDetailAdapter = OrderDetailAdapter(orderDetailList)
        recyclerView.adapter = orderDetailAdapter

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

        val orderId = intent.getIntExtra("ORDER_ID", -1)

        if (orderId != -1) {
            // Quan sát dữ liệu từ ViewModel
            viewModel.orderDetails.observe(this, Observer { details ->
                progressBar.visibility = View.GONE
                details?.let {
                    orderDetailList.clear()
                    orderDetailList.addAll(it)
                    orderDetailAdapter.notifyDataSetChanged()
                }
            })

            viewModel.error.observe(this, Observer { error ->
                progressBar.visibility = View.GONE
                // Xử lý lỗi nếu có
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            })

            // Gọi hàm trong ViewModel để lấy dữ liệu
            progressBar.visibility = View.VISIBLE
            viewModel.getOrderDetailsByOrderId(orderId)
        } else {
            // Xử lý khi không có Order ID
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
