package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Model.OrderDetailModel
import com.example.ban_giay_mobile.Model.OrderModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityOrderDetailBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.UserRepository
import com.example.ban_giay_mobile.Model.UserModel
import com.example.ban_giay_mobile.repository.OrderRepository

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            BannerRepository(RetrofitClient.instance),
            BrandRepository(RetrofitClient.instance),
            ItemRepository(RetrofitClient.instance),
            FavouriteRepository(RetrofitClient.instance),
            UserRepository(RetrofitClient.instance),
            OrderRepository(RetrofitClient.instance)
        )
    }

    private lateinit var nameUser: EditText
    private lateinit var phoneUser: EditText
    private lateinit var addressUser: EditText
    private lateinit var buyButton: Button
    private lateinit var cartItems: ArrayList<ItemModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ánh xạ các trường TextView và EditText từ layout
        nameUser = findViewById(R.id.nameUser)
        phoneUser = findViewById(R.id.phoneUser)
        addressUser = findViewById(R.id.addressUser)
        buyButton = findViewById(R.id.BuyBtn)

        // Nhận giá trị totalAmount từ Intent
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)
        cartItems = intent.getParcelableArrayListExtra("cartItems") ?: arrayListOf()
        val totalTextView = findViewById<TextView>(R.id.totalTxt)
        totalTextView.text = String.format("$%.2f", totalAmount)

        // Lấy userId từ SharedPreferences
        val userId = getUserId()
        if (userId != -1) {
            // Tải dữ liệu người dùng
            loadUserData(userId)
        } else {
            // Xử lý trường hợp không tìm thấy userId
            Toast.makeText(this, "Người dùng không hợp lệ", Toast.LENGTH_SHORT).show()
        }

        buyButton.setOnClickListener {
            // Xây dựng OrderModel từ thông tin trong EditText và cartItems
            val name = nameUser.text.toString()
            val phone = phoneUser.text.toString()
            val address = addressUser.text.toString()
            val totalPrice = totalAmount
            val status = "pending" // Trạng thái mặc định là pending

            // Giả sử bạn đã có OrderDetailModel như trong CartActivity
            val orderDetails = cartItems.map {
                OrderDetailModel(
                    detailId = 0, // Đặt giá trị mặc định hoặc lấy từ cơ sở dữ liệu nếu có
                    orderId = 0, // Đặt giá trị mặc định hoặc lấy từ cơ sở dữ liệu nếu có
                    productName = it.title ?: "",
                    quantity = it.quantity ?: 0,
                    size = it.size ?: ""
                )
            }

            val order = OrderModel(
                orderId = 0,
                name = name,
                phone = phone,
                address = address,
                totalPrice = totalPrice,
                status = status,
                createdAt = "", // Bạn có thể đặt giá trị này nếu cần
                orderDetails = orderDetails,
                userId = userId
            )

            // Log thông tin order và orderDetails
//            Log.d("OrderDetailActivity", "Order Model: $order")
//            Log.d("OrderDetailActivity", "Order Details: ${order.orderDetails}")

            // Gọi hàm saveOrder
            saveOrder(order)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun getUserId(): Int {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        return sharedPref.getInt("USER_ID", -1)
    }

    private fun loadUserData(userId: Int) {
        viewModel.loadUserById(userId)

        viewModel.user.observe(this, Observer { user ->
            if (user != null) {
                handleUserResponse(user)
            }
        })

        viewModel.error.observe(this, Observer { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleUserResponse(userModel: UserModel) {
        // Cập nhật giao diện người dùng với thông tin người dùng
        nameUser.setText(userModel.firstname ?: "")
        phoneUser.setText(userModel.phone ?: "")
        addressUser.setText(userModel.address ?: "")
    }

    private fun saveOrder(order: OrderModel) {
        viewModel.createOrder(order)
        viewModel.createOrderResult.observe(this) { response ->
            if (response.isSuccessful) {
                Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
                // Chuyển về MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                val errorMessage = response?.errorBody()?.string() ?: "Tạo đơn hàng thất bại: Unknown error"
                Toast.makeText(this, "Tạo đơn hàng thất bại: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun getCurrentTime(): String {
        // Hàm để lấy thời gian hiện tại dưới dạng String, có thể sử dụng SimpleDateFormat
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }

}
