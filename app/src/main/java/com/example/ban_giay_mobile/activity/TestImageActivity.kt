package com.example.ban_giay_mobile.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.Model.SliderModel
import com.example.ban_giay_mobile.databinding.ActivityTestImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load image from database URL
        loadImage()
    }

    private fun loadImage() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getBanners()
                if (response.isSuccessful) {
                    val banners = response.body()
                    val firstBanner = banners?.firstOrNull()
                    withContext(Dispatchers.Main) {
                        firstBanner?.let {
                            Glide.with(this@TestImageActivity)
                                .load(it.url)
                                .into(binding.imageViewTest)
                        }
                    }
                } else {
                    Log.e("TestImageActivity", "Failed to get banners: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TestImageActivity", "Exception occurred: ${e.message}")
            }
        }
    }
}
