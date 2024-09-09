package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.BrandAdapter
import com.example.ban_giay_mobile.adapter.ProductAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityProductAllBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class ProductActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityProductAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo các repository
        val bannerRepository = BannerRepository(RetrofitClient.instance)
        val brandRepository = BrandRepository(RetrofitClient.instance)
        val itemRepository = ItemRepository(RetrofitClient.instance)
        val favouriteRepository = FavouriteRepository(RetrofitClient.instance)
        val userRepository = UserRepository(RetrofitClient.instance)
        val orderRepository = OrderRepository(RetrofitClient.instance)

        // Sử dụng MainViewModelFactory để tạo MainViewModel
        val viewModelFactory = MainViewModelFactory(bannerRepository, brandRepository, itemRepository, favouriteRepository, userRepository, orderRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        initBrands()
        initPopularItems()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchItems(query)
                } else {
                    loadAllProducts()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.viewCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Lấy tham chiếu đến viewFavourite
        val viewFavourite = findViewById<ImageView>(R.id.viewFavourite)

        // Xử lý sự kiện click
        viewFavourite.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBrands() {
        binding.progressBarBrand.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer { items ->
            binding.viewBrand.layoutManager = LinearLayoutManager(this@ProductActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter = BrandAdapter(items) { brand ->
                if (brand == null) {
                    loadAllProducts() // Show all products if no brand is selected
                } else {
                    viewModel.loadItemsByBrand(brand.id) // Load products based on selected brand
                }
            }

            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBrands()
    }

    private fun initPopularItems() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.items.observe(this, Observer { items ->
            binding.viewPopular.layoutManager = GridLayoutManager(this@ProductActivity, 2)
            binding.viewPopular.adapter = ProductAdapter(items)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadItems()
    }

    private fun loadAllProducts() {
        viewModel.items.observe(this, Observer { items ->
            if (items.isNotEmpty()) {
                binding.viewPopular.layoutManager = GridLayoutManager(this@ProductActivity, 2)
                binding.viewPopular.adapter = ProductAdapter(items)
            } else {
                // Handle empty list case
                binding.viewPopular.adapter = ProductAdapter(emptyList())
            }
        })
        viewModel.loadItems()
    }


    private fun searchItems(query: String) {
            viewModel.loadItemsBySearch(query)
            viewModel.items.observe(this, Observer { items ->
                if (items.isNotEmpty()) {
                    binding.viewPopular.layoutManager = GridLayoutManager(this@ProductActivity, 2)
                    binding.viewPopular.adapter = ProductAdapter(items)
                } else {
                    binding.viewPopular.adapter = ProductAdapter(emptyList())
                }
            })
    }

}
