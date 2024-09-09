package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.FavouriteAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityFavouriteBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Model.FavouriteModel
import androidx.lifecycle.Observer
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var favouriteAdapter: FavouriteAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        val userId = getUserId() // Lấy userId từ SharedPreferences hoặc nguồn khác
        viewModel.loadFavourites(userId)

        viewModel.favourites.observe(this) { favourites ->
            Log.d("Favourites", "Favourites: $favourites")
            val itemIds = favourites.map { it.itemId }
            viewModel.loadItemsDetails(itemIds)
        }

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

        binding.viewFavourite2.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }

        viewModel.items.observe(this) { items ->
            favouriteAdapter.updateItems(items)
            updateEmptyTextVisibility(viewModel.favourites.value ?: emptyList())
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun updateEmptyTextVisibility(favourites: List<FavouriteModel>) {
        if (favourites.isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.viewFavourite.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.viewFavourite.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        favouriteAdapter = FavouriteAdapter(emptyList()) { item ->
            // Handle delete item
            val userId = getUserId() // Get userId from SharedPreferences or other source
            viewModel.removeFavourite(userId, item.id)
            Toast.makeText(this, "Sản phẩm đã được xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
        }
        binding.viewFavourite.adapter = favouriteAdapter
        binding.viewFavourite.layoutManager = LinearLayoutManager(this)
    }

    private fun getUserId(): Int {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        return sharedPref.getInt("USER_ID", -1)
    }
}
