package com.example.ban_giay_mobile.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.databinding.ActivityAddProductBinding
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
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

    private var selectedBrandId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Spinner with Brands
        initBrands()

        // Handle Add Product button click
        binding.addProductBtn.setOnClickListener {
            val name = binding.ProductName.text.toString()
            val price = binding.ProductPrice.text.toString().toInt()
            val description = binding.Description.text.toString()
            val pictureUrl = binding.PicUrl.text.toString()
            val rating = binding.Rating.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotEmpty() && price > 0 && description.isNotEmpty() && pictureUrl.isNotEmpty() && rating >= 0 && selectedBrandId != null) {
                val item = ItemModel(
                    title = name,
                    price = price,
                    description = description,
                    picUrl = pictureUrl,
                    rating = rating,
                    brandId = selectedBrandId!!
                )
                viewModel.addItem(item)
            } else {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        // Observe errors and success
        viewModel.item.observe(this, Observer {
            // Handle success (e.g., show a success message or redirect to another activity)
            Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show()
            finish()
        })

        viewModel.error.observe(this, Observer { error ->
            // Handle error
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })


    }

//    private fun initBrands() {
//        // Show progress bar while loading
//        binding.progressBarBrand.visibility = View.VISIBLE
//
//        // Observe brands LiveData
//        viewModel.brands.observe(this, Observer { brands ->
//            if (brands != null) {
//                // Map brands to a list of titles for the spinner
//                val brandTitles = brands.map { it.title }
//
//                // Create an ArrayAdapter with the brand titles
//                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, brandTitles)
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.Category.adapter = adapter
//            }
//
//            // Hide progress bar after loading
//            binding.progressBarBrand.visibility = View.GONE
//        })
//
//        // Load brands from the repository
//        viewModel.loadBrands()
//    }

    private fun initBrands() {
        binding.progressBarBrand.visibility = View.VISIBLE

        viewModel.brands.observe(this, Observer { brands ->
            if (brands != null) {
                val brandTitles = brands.map { it.title }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, brandTitles)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.Category.adapter = adapter

                binding.Category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        selectedBrandId = brands[position].id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        selectedBrandId = null
                    }
                }
            }

            binding.progressBarBrand.visibility = View.GONE
        })

        viewModel.loadBrands()
    }
}
