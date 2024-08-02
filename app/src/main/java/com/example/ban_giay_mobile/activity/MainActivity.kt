package com.example.ban_giay_mobile.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.ban_giay_mobile.Model.SliderModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.SliderAdapter
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private val viewModel= MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBanner()
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banner.observe(this, Observer { items->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanner()
    }
    private fun banners(images:List<SliderModel>){
        binding.viewpageSilder.adapter= SliderAdapter(images, binding.viewpageSilder)
        binding.viewpageSilder.clipToPadding = false
        binding.viewpageSilder.clipChildren = false
        binding.viewpageSilder.offscreenPageLimit = 3
        binding.viewpageSilder.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositionPageTransformer = CompositePageTransformer().apply{
            addTransformer (MarginPageTransformer(40))
        }
        binding.viewpageSilder.setPageTransformer(compositionPageTransformer)
        if(images.size>1){
            binding.dotIndicator.visibility= View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpageSilder)
        }


    }
}