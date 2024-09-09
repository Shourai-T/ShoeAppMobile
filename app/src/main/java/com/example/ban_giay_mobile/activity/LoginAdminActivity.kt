package com.example.ban_giay_mobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.Model.AdminModel
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginAdminActivity : AppCompatActivity() {

    private lateinit var usernameTxt: EditText
    private lateinit var passTxt: EditText
    private lateinit var loginBtn: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        usernameTxt = findViewById(R.id.userTxt)
        passTxt = findViewById(R.id.passTxt)
        loginBtn = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val username = usernameTxt.text.toString()
            val password = passTxt.text.toString()

            if (validateInput(username, password)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitClient.instance.loginAdmin(AdminModel(username, password))
                    handleLoginResponse(response)
                }
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Điền thông tin đăng nhập", Toast.LENGTH_SHORT).show()
            return false
        }
        if (username.isEmpty()) {
            usernameTxt.error = "Username is required"
            return false
        }
        if (password.isEmpty()) {
            passTxt.error = "Password is required"
            return false
        }
        return true
    }

    private fun handleLoginResponse(response: Response<ApiService.LoginAdminResponse>) {
        runOnUiThread {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                Log.d("Login", "handleLoginResponse: $loginResponse ")
                val admin = loginResponse?.admin
                if (admin != null) {
                    Log.d("LoginAdminActivity", "Admin username: ${admin.username}")

                    // Lưu thông tin Admin vào SharedPreferences
                    val sharedPref = getSharedPreferences("ADMIN_PREF", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("ADMIN_USERNAME", admin.username)
                        apply()
                    }

                    // Chuyển tới AdminActivity (hoặc màn hình quản lý)
                    startActivity(Intent(this, AdminActivity::class.java))
                    finish()
                }
            } else {
                // Xử lý lỗi đăng nhập
                when (response.code()) {
                    404 -> {
                        // Tên đăng nhập không tồn tại
                        usernameTxt.error = "Tên đăng nhập không tồn tại."
                    }
                    401 -> {
                        // Mật khẩu không đúng
                        passTxt.error = "Mật khẩu không chính xác."
                    }
                    else -> {
                        // Xử lý lỗi chung
                        Toast.makeText(this, "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
