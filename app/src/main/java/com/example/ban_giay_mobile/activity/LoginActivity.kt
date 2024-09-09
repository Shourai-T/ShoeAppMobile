package com.example.ban_giay_mobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.Model.UserModel
import com.example.ban_giay_mobile.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passTxt: EditText
    private lateinit var loginBtn: Button
    private lateinit var tvSignUp: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailTxt = findViewById(R.id.emailTxt)
        passTxt = findViewById(R.id.passTxt)
        loginBtn = findViewById(R.id.loginBtn)
        tvSignUp = findViewById(R.id.tvSignIn)

        loginBtn.setOnClickListener {
            val email = emailTxt.text.toString()
            val password = passTxt.text.toString()

            if (validateInput(email, password)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitClient.instance.loginUser(UserModel(0, "", email, password, "", "", "", ""))
                    handleLoginResponse(response)
                }
            }
        }


        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Điền thông tin đăng nhập", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            emailTxt.error = "Email is required"
            return false
        }
        if (password.isEmpty()) {
            passTxt.error = "Password is required"
            return false
        }
        return true
    }


    private fun handleLoginResponse(response: Response<ApiService.LoginResponse>) {
        runOnUiThread {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                val user = loginResponse?.user
                if (user != null) {
                    Log.d("Response", "bodyUser: $user")
                    Log.d("LoginActivity", "User ID: ${user.id}")

                    // Lưu userId vào SharedPreferences
                    val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putInt("USER_ID", user.id)
                        apply()
                    }

                    // Chuyển tới MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            } else {
                // Xử lý lỗi đăng nhập
                when (response.code()) {
                    404 -> {
                        // Email không tồn tại
                        emailTxt.error = "Email không tồn tại."
                    }
                    401 -> {
                        // Mật khẩu không đúng
                        passTxt.error = "Mật khẩu không chính xác."
                    }
                    else -> {
                        // Xử lý lỗi đăng nhập chung
                        Toast.makeText(this, "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}
