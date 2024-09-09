package com.example.ban_giay_mobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ban_giay_mobile.Model.ItemImagesModel
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Model.SizeModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.ColorAdapter
import com.example.ban_giay_mobile.adapter.DetailAdapter
import com.example.ban_giay_mobile.adapter.SizeAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityDetailBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
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

    private var userId: Int = -1
    private var selectedSize: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy userId từ SharedPreferences
        userId = getUserId()

        // Log giá trị userId
        Log.d("DetailActivity", "User ID: $userId")

        if (userId == -1) {
            Toast.makeText(this, "Rồi xong lỗi rồi", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val itemId = intent.getIntExtra("ITEM_ID", -1)
        if (itemId != -1) {
            viewModel.loadItemById(itemId)
            viewModel.item.observe(this, Observer { item ->
                if (item != null) {
                    setupSlider(item)
                    setupColorList(item.id)
                    setupSizeList(item.id)
                    updateItemDetails(item)
                }
            })

            Log.d("DetailActivity", "Item Id: ${itemId} ")
        }
        // Set up back button click listener
        binding.backBtn.setOnClickListener {
            finish() // Quay lại trang trước đó
        }

//        binding.addToCartBtn.setOnClickListener {
//                val intent = Intent(this, CartActivity::class.java)
//                intent.putExtra("ITEM_ID", itemId)
//                startActivity(intent)
//        }

        binding.addToCartBtn.setOnClickListener {
            if (selectedSize != null) {
                val intent = Intent(this, CartActivity::class.java)
                intent.putExtra("ITEM_ID", itemId)
                intent.putExtra("ITEM_SIZE", selectedSize)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Hãy chọn kích cỡ sản phẩm!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }


        binding.favBtn.setOnClickListener {
            if (itemId != -1) {
                addProductToFavourites(userId, itemId)
            }
        }


        viewModel.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })


    }

    private fun getUserId(): Int {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        return sharedPref.getInt("USER_ID", -1)
    }

    private fun updateItemDetails(item: ItemModel) {
        binding.titleTxt.text = item.title
        binding.priceTxt.text = "$${item.price}"
        binding.descriptionTxt.text = item.description
        binding.ratingTxt.text = item.rating.toString()
    }

    private fun setupSlider(item: ItemModel) {
        viewModel.loadItemImages(item.id)
        viewModel.itemImages.observe(this, Observer { itemImages ->
            if (itemImages.isNotEmpty()) {
                val imageUrls = itemImages.map { it.picUrl }

                val adapter = DetailAdapter(imageUrls) // Sử dụng List<String>
                binding.slider.adapter = adapter
                binding.detaildotIndicator.setViewPager2(binding.slider)
                binding.detaildotIndicator.visibility = if (imageUrls.size > 1) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setupColorList(itemId: Int) {
        viewModel.loadItemImages(itemId) // Tải lại hình ảnh nếu chưa có trong viewModel
        viewModel.itemImages.observe(this, Observer { itemImages ->
            if (itemImages.isNotEmpty()) {
                val colorList = itemImages.map { ItemImagesModel(id = it.id, item_id = itemId, picUrl = it.picUrl) }

                val colorAdapter = ColorAdapter(colorList)
                binding.colorList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.colorList.adapter = colorAdapter
            }
        })
    }

    private fun setupSizeList(itemId: Int) {
        viewModel.loadItemSizes(itemId)
        viewModel.itemSizes.observe(this, Observer { sizes ->
            if (sizes.isNotEmpty()) {
                val sizeAdapter = SizeAdapter(sizes) { size ->
                    onSizeSelected(size)
                }
                binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.sizeList.adapter = sizeAdapter
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSizeSelected(size: SizeModel) {
        selectedSize = size.size // or however you want to handle the selected size
        Log.d("onSizeSelected", "onSizeSelected: &=${selectedSize}")
        binding.sizeList.adapter?.notifyDataSetChanged() // Refresh the adapter to update the UI
    }

//    private fun setupSizeList(itemId: Int) {
//        viewModel.loadItemSizes(itemId)
//        viewModel.itemSizes.observe(this, Observer { sizes ->
//            if (sizes.isNotEmpty()) {
//                val sizeAdapter = SizeAdapter(sizes)
//                binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//                binding.sizeList.adapter = sizeAdapter
//            }
//        })
//    }

//    private fun addProductToFavourites(userId: Int, itemId: Int) {
//        viewModel.addFavourite(userId, itemId)
//        viewModel.favouriteAdded.observe(this, Observer { response ->
//            if (response.isSuccessful) {
//                Toast.makeText(this, "Sản phẩm đã được thêm vào yêu thích", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Thêm sản phẩm vào yêu thích thất bại", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun addProductToFavourites(userId: Int, itemId: Int) {
        viewModel.checkIfItemIsFavourited(userId, itemId)
        viewModel.isFavourited.observe(this, Observer { isFavourited ->
            if (isFavourited) {
                Toast.makeText(this, "Sản phẩm đã có trong danh sách yêu thích", Toast.LENGTH_SHORT).show()
            } else {
                // Nếu sản phẩm chưa có trong danh sách yêu thích, thêm sản phẩm vào danh sách yêu thích
                viewModel.addFavourite(userId, itemId)
                viewModel.favouriteAdded.observe(this, Observer { response ->
                    if (response.isSuccessful) {
                        Toast.makeText(this, "Sản phẩm đã được thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Thêm sản phẩm vào yêu thích thất bại", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
    }

}
