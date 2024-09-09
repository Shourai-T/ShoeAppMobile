package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityRegisterBinding
import com.example.ban_giay_mobile.Model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener {
            val username = binding.userTxt.text.toString().trim()
            val email = binding.emailTxt.text.toString().trim()
            val password = binding.passTxt.text.toString().trim()
            val confirmPassword = binding.confirmPassTxt.text.toString().trim()

            if (validateInput(username, email, password, confirmPassword)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitClient.instance.registerUser(UserModel(0, username, email, password, "", "", "", ""))
                    Log.d("Response", "onCreate: $response ")
                    handleRegisterResponse(response)
                }
            }
        }

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        if (username.isEmpty()) {
            showToast("Vui lòng nhập username")
            return false
        }
        if (email.isEmpty()) {
            showToast("Vui lòng nhập email")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email không đúng định dạng")
            return false
        }
        if (password.isEmpty()) {
            showToast("Vui lòng nhập mật khẩu")
            return false
        }
        if (password.length < 6 || password.length > 24) {
            showToast("Mật khẩu phải nằm trong khoảng 6 đến 24 ký tự")
            return false
        }
        if (confirmPassword.isEmpty()) {
            showToast("Vui lòng xác nhận mật khẩu")
            return false
        }
        if (password != confirmPassword) {
            showToast("Xác nhận mật khẩu không trùng")
            return false
        }
        return true
    }

    private fun handleRegisterResponse(response: Response<Void>) {
        runOnUiThread {
            if (response.isSuccessful) {
                // Handle successful registration
                showToast("Đăng ký thành công")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                // Handle registration failure
                val errorBody = response.errorBody()?.string()
                // Handle registration failure
                when (response.code()) {
                    400 -> {
                        if (errorBody?.contains("Username already in use") == true) {
                            showToast("Username đã tồn tại")
                        }
                        else if (errorBody?.contains("Email already in use") == true) {
                            showToast("Email đã tồn tại")
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
