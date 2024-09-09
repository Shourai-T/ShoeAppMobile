package com.example.ban_giay_mobile.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.ProductManageAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityProductManageBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class ProductManageActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityProductManageBinding
    private var selectedItem: ItemModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductManageBinding.inflate(layoutInflater)
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

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        binding.updateBtn.setOnClickListener {
            if (selectedItem == null) {
                // Hiển thị thông báo khi chưa chọn sản phẩm
                Toast.makeText(this, "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show()
            } else {
                selectedItem?.let {
                    val intent = Intent(this, EditProductActivity::class.java).apply {
                        putExtra("ITEM_ID", it.id)
                    }
                    startActivity(intent)

                    // Đặt selectedItem về null sau khi startActivity
                    selectedItem = null
                }
            }
        }

        binding.deleteBtn.setOnClickListener {
            if (selectedItem == null) {
                // Hiển thị thông báo khi chưa chọn sản phẩm
                Toast.makeText(this, "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show()
            } else {
                selectedItem?.let { item ->
                    // Hiển thị AlertDialog để xác nhận xóa
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Xác nhận xóa")
                    builder.setMessage("Bạn có chắc chắn muốn xóa ${item.title} không?")

                    // Nếu admin chọn "Có", thì tiến hành xóa sản phẩm
                    builder.setPositiveButton("Có") { dialog, _ ->
                        viewModel.deleteItem(item.id)
                        Toast.makeText(this, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show()
                        // Đặt selectedItem về null sau khi xóa
                        initPopularItems()
                        selectedItem = null
                        dialog.dismiss() // Đóng dialog sau khi xóa
                    }

                    // Nếu admin chọn "Không", chỉ đóng dialog
                    builder.setNegativeButton("Không") { dialog, _ ->
                        dialog.dismiss() // Đóng dialog mà không làm gì
                        selectedItem = null
                    }

                    // Hiển thị dialog
                    builder.create().show()
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        initPopularItems()
        selectedItem = null
    }

    private fun initPopularItems() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.items.observe(this, Observer { items ->
            binding.viewPopular.layoutManager = GridLayoutManager(this@ProductManageActivity, 2)
            binding.viewPopular.adapter = ProductManageAdapter(items) { item ->
                selectedItem = item
            }
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadItems()
    }

    private fun loadAllProducts() {
        viewModel.items.observe(this, Observer { items ->
            if (items.isNotEmpty()) {
                binding.viewPopular.layoutManager = GridLayoutManager(this@ProductManageActivity, 2)
                binding.viewPopular.adapter = ProductManageAdapter(items) { item ->
                    selectedItem = item
                }
            } else {
                binding.viewPopular.adapter = ProductManageAdapter(emptyList()) { item ->
                    selectedItem = item
                }
            }
        })
        viewModel.loadItems()
    }

    private fun searchItems(query: String) {
        viewModel.loadItemsBySearch(query)
        viewModel.items.observe(this, Observer { items ->
            if (items.isNotEmpty()) {
                binding.viewPopular.layoutManager = GridLayoutManager(this@ProductManageActivity, 2)
                binding.viewPopular.adapter = ProductManageAdapter(items) { item ->
                    selectedItem = item
                }
            } else {
                binding.viewPopular.adapter = ProductManageAdapter(emptyList()) { item ->
                    selectedItem = item
                }
            }
        })
    }


}
