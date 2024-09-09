package com.example.ban_giay_mobile.activity


import SliderAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Model.SliderModel
import com.example.ban_giay_mobile.Model.UserModel
import com.example.ban_giay_mobile.R
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.BrandAdapter
import com.example.ban_giay_mobile.adapter.PopularAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityMainBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var userId: Int = -1
    private lateinit var firstName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstName = findViewById(R.id.viewName)

        // Khởi tạo BannerRepository
        val bannerRepository = BannerRepository(RetrofitClient.instance)
        val brandRepository = BrandRepository(RetrofitClient.instance)
        val itemRepository = ItemRepository(RetrofitClient.instance)
        val favouriteRepository = FavouriteRepository(RetrofitClient.instance)
        val userRepository = UserRepository(RetrofitClient.instance)
        val orderRepository = OrderRepository(RetrofitClient.instance)
        // Sử dụng MainViewModelFactory để tạo MainViewModel
        val viewModelFactory = MainViewModelFactory(bannerRepository, brandRepository, itemRepository, favouriteRepository, userRepository, orderRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        initBanner()
        initBrand()
        initPopular()

        // Lấy userId từ SharedPreferences
        userId = getUserId()

        // Kiểm tra trạng thái đăng nhập
        checkLoginState()

        // Log giá trị userId
        Log.d("MainActivity", "User ID: $userId")

        if (userId == -1) {
            // Handle trường hợp userId không tồn tại (người dùng chưa đăng nhập)
            Toast.makeText(this, "Rồi xong lỗi rồi", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        // Thêm sự kiện click vào "See all"
        binding.seeAllProduct.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
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

        // Lấy tham chiếu đến viewFavourite
        val viewFavourite = findViewById<ImageView>(R.id.viewFavourite)

        // Xử lý sự kiện click
        viewFavourite.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }

        loadUserData(userId)

    }

    private fun loadUserData(userId: Int) {
        viewModel.loadUserById(userId) // Gọi hàm để tải dữ liệu người dùng

        // Quan sát dữ liệu người dùng từ ViewModel
        viewModel.user.observe(this, Observer { user ->
            if (user != null) {
                initUserName(user) // Xử lý dữ liệu người dùng
            }
        })

        // Quan sát lỗi từ ViewModel
        viewModel.error.observe(this, Observer { error ->
            if (error != null) {
                // Xử lý lỗi, chẳng hạn như hiển thị thông báo lỗi
                Log.e("UserActivity", error)
            }
        })
    }

    private fun getUserId(): Int {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        return sharedPref.getInt("USER_ID", -1)
    }

    private fun checkLoginState() {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId == -1) {
            // Người dùng chưa đăng nhập, chuyển hướng đến LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
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
            binding.viewBrand.adapter = BrandAdapter(items) { brand ->
                if (brand == null) {
                    initPopular() // Show all products if no brand is selected
                } else {
                    viewModel.loadItemsByBrand(brand.id) // Load products based on selected brand
                }
            }
            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBrands()
    }
    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE

        // Observe popular items from ViewModel
        viewModel.items.observe(this) { items ->
            val top4Items = filterTop4ItemsByRating(items)
            binding.viewPopular.layoutManager = GridLayoutManager(this, 2)
            binding.viewPopular.adapter = PopularAdapter(top4Items)
            binding.progressBarPopular.visibility = View.GONE
        }

        // Load popular items
        viewModel.loadItems()  // Ensure you have a method in ViewModel to load popular items
    }

    private fun filterTop4ItemsByRating(items: List<ItemModel>): List<ItemModel> {
        return items
            .sortedByDescending { it.rating } // Sắp xếp sản phẩm theo rating giảm dần
            .take(4) // Lấy 4 sản phẩm đầu tiên
    }

    private fun initUserName(userModel: UserModel) {
        if (userModel.firstname == null) {
            firstName.text = userModel.username
        } else {
            firstName.text = userModel.firstname
        }
    }
}