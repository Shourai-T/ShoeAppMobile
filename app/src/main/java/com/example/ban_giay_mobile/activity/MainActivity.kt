package com.example.ban_giay_mobile.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.ban_giay_mobile.Model.SliderModel
import com.example.ban_giay_mobile.adapter.SliderAdapter
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.adapter.BrandAdapter
import com.example.ban_giay_mobile.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private val viewModel= MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBanner()
        initBrand()
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items->
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
    private fun initBrand() {
        binding.progressBarBrand.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer { items->
            binding.viewBrand.layoutManager= LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter= BrandAdapter(items)
            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBrand()
    }
    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer { items->
            binding.viewBrand.layoutManager= LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter= BrandAdapter(items)
            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBrand()
    }
}