package com.example.ban_giay_mobile.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
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
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.UserRepository
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.Model.UserModel
import com.example.ban_giay_mobile.databinding.ActivityEditProfileBinding
import com.example.ban_giay_mobile.databinding.ActivityMainBinding
import com.example.ban_giay_mobile.repository.OrderRepository

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
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
    private lateinit var nameUser: EditText
    private lateinit var emailUser: TextView
    private lateinit var phoneUser: EditText
    private lateinit var addressUser: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var submitButton: AppCompatButton
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ánh xạ các TextView và EditText từ layout
        firstName = findViewById(R.id.viewName)
        nameUser = findViewById(R.id.nameUser)
        emailUser = findViewById(R.id.viewEmail)
        phoneUser = findViewById(R.id.phoneUser)
        addressUser = findViewById(R.id.addressUser)
        profileImageView = findViewById(R.id.imageView10)
        submitButton = findViewById(R.id.submitBtn)

        // Lấy userId từ SharedPreferences
        userId = getUserId()

        if (userId == -1) {
            Toast.makeText(this, "Người dùng không hợp lệ", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Tải dữ liệu người dùng và thiết lập giá trị cho các EditText
        loadUserData(userId)

        // Xử lý sự kiện khi nhấn nút Submit
        submitButton.setOnClickListener {
            val firstname = nameUser.text.toString()
            val phone = phoneUser.text.toString()
            val address = addressUser.text.toString()

            // Gọi hàm để cập nhật thông tin người dùng
            updateUser(userId, firstname, phone, address)
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
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleUserResponse(userModel: UserModel) {
        // Cập nhật giao diện người dùng với thông tin người dùng
        if (userModel.firstname.isNullOrEmpty()) {
            firstName.text = userModel.username
            nameUser.setText(userModel.username)
        } else {
            firstName.text = userModel.firstname
            nameUser.setText(userModel.firstname)
        }

        emailUser.text = userModel.email
        phoneUser.setText(userModel.phone ?: "")
        addressUser.setText(userModel.address ?: "")

        Glide.with(this)
            .load(R.drawable.intro_logo)
            .into(profileImageView)
    }

    private fun updateUser(userId: Int, firstname: String, phone: String, address: String) {
        // Tạo đối tượng UserModel với các giá trị mới
        val updatedUser = UserModel(
            id = userId,
            firstname = firstname,
            phone = phone,
            address = address
        )

        // Gọi hàm updateUser từ ViewModel với đối tượng UserModel
        viewModel.updateUserById(userId, updatedUser)
        Log.d("updateUser", "updateUser: $updatedUser")
        // Quan sát kết quả cập nhật từ ViewModel
        viewModel.updateUserResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show()
                finish() // Quay lại activity trước đó
            } else {
                Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
