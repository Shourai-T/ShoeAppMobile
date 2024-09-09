package com.example.ban_giay_mobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.repository.UserRepository
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.Model.UserModel
import com.example.ban_giay_mobile.databinding.ActivityMainBinding
import com.example.ban_giay_mobile.databinding.ActivityProfileBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository

class UserActivity : AppCompatActivity() {
    private var userId: Int = -1
    private lateinit var binding: ActivityProfileBinding
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
    private lateinit var firstName: TextView
    private lateinit var nameUser: TextView
    private lateinit var emailUser: TextView
    private lateinit var phoneUser: TextView
    private lateinit var addressUser: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var logoutBtn: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ánh xạ các TextView từ layout
        firstName = findViewById(R.id.viewName)
        nameUser = findViewById(R.id.nameUser)
        emailUser = findViewById(R.id.viewEmail)
        phoneUser = findViewById(R.id.phoneUser)
        addressUser = findViewById(R.id.addressUser)
        profileImageView = findViewById(R.id.imageView10)
        logoutBtn = findViewById(R.id.logoutBtn)


        // Lấy userId từ SharedPreferences
        userId = getUserId()

        // Log giá trị userId
        Log.d("MainActivity", "User ID: $userId")

        if (userId == -1) {
            // Handle trường hợp userId không tồn tại (người dùng chưa đăng nhập)
            Toast.makeText(this, "Rồi xong lỗi rồi", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadUserData(userId)

        binding.viewHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.viewCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        binding.viewUser.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        // Lấy tham chiếu đến viewFavourite
        val viewFavourite = findViewById<ImageView>(R.id.viewFavourite)

        // Xử lý sự kiện click
        viewFavourite.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }

        val editProfileBtn = findViewById<AppCompatButton>(R.id.edit_profile_btn)
        editProfileBtn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện nhấn nút Logout
        logoutBtn.setOnClickListener {
            // Xoá thông tin đăng nhập khỏi SharedPreferences
            val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("USER_ID")
                apply()
            }

            // Chuyển đến LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        // Tải lại dữ liệu người dùng khi Activity được quay lại
        loadUserData(userId)
    }

    private fun getUserId(): Int {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        return sharedPref.getInt("USER_ID", -1)
    }

    private fun loadUserData(userId: Int) {
        viewModel.loadUserById(userId) // Gọi hàm để tải dữ liệu người dùng

        // Quan sát dữ liệu người dùng từ ViewModel
        viewModel.user.observe(this, Observer { user ->
            if (user != null) {
                handleUserResponse(user) // Xử lý dữ liệu người dùng
            }
        })

        // Quan sát lỗi từ ViewModel
        viewModel.error.observe(this, Observer { error ->
            if (error != null) {
                // Xử lý lỗi, chẳng hạn như hiển thị thông báo lỗi
                Log.e("UserActivity", error)
            }
        })
    }

    private fun handleUserResponse(userModel: UserModel) {
        // Cập nhật giao diện người dùng với thông tin người dùng
        emailUser.text = userModel.email
        if (userModel.firstname == null) {
            firstName.text = userModel.username
            nameUser.text = "Trống"
        } else {
            firstName.text = userModel.username
            nameUser.text = userModel.firstname
        }
        if (userModel.phone != null) {
            phoneUser.text = userModel.phone
        } else {
            phoneUser.text = "Trống"
        }
        if (userModel.address != null) {
            addressUser.text = userModel.address
        } else {
            addressUser.text = "Trống"
        }

        Glide.with(this)
            .load(R.drawable.intro_logo)
            .into(profileImageView)

    }
}
