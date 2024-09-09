package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ban_giay_mobile.R

class AdminActivity : AppCompatActivity() {

    private lateinit var productManageBtn: Button
    private lateinit var orderManageBtn: Button
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        // Ánh xạ các nút từ layout
        productManageBtn = findViewById(R.id.button7)
        orderManageBtn = findViewById(R.id.button8)
        logoutBtn = findViewById(R.id.logoutBtn)

        // Xử lý sự kiện khi bấm nút "Product Manage"
        productManageBtn.setOnClickListener {
            val intent = Intent(this, ProductManageActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện khi bấm nút "Order Manage"
        orderManageBtn.setOnClickListener {
            val intent = Intent(this, OrderManageActivity::class.java)
            startActivity(intent)
        }

        logoutBtn.setOnClickListener {
            // Xoá thông tin đăng nhập khỏi SharedPreferences
            val sharedPref = getSharedPreferences("ADMIN_PREF", MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("ADMIN_USERNAME")
                apply()
            }

            // Chuyển đến LoginActivity
            val intent = Intent(this, LoginAdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
