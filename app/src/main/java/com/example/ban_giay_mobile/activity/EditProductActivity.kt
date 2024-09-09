package com.example.ban_giay_mobile.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.BrandAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityAddProductBinding
import com.example.ban_giay_mobile.databinding.ActivityEditProductBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class EditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProductBinding
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
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemId = intent.getIntExtra("ITEM_ID", -1)
        if (itemId != -1) {
            viewModel.loadItemById(itemId)
            viewModel.item.observe(this, Observer { item ->
                item?.let {
                    binding.ProductName.setText(it.title)
                    binding.ProductPrice.setText(it.price.toString())
                    binding.Description.setText(it.description)
                    binding.PicUrl.setText(it.picUrl)
                    binding.Rating.setText((it.rating.toString()))
                }
            })
        }

        binding.updateProductBtn.setOnClickListener {
            val updatedItem = ItemModel(
                id = itemId,
                title = binding.ProductName.text.toString(),
                price = binding.ProductPrice.text.toString().toInt(),
                description = binding.Description.text.toString(),
                picUrl = binding.PicUrl.text.toString(),
                rating = binding.Rating.text.toString().toDouble()
            )
            viewModel.updateItem(itemId, updatedItem)
            viewModel.item.observe(this, Observer {
                Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                finish() // Quay lại màn hình quản lý sản phẩm
            })
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }


}

