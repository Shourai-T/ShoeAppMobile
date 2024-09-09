package com.example.ban_giay_mobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ban_giay_mobile.ViewModel.MainViewModelFactory
import com.example.ban_giay_mobile.adapter.CartAdapter
import com.example.ban_giay_mobile.api.RetrofitClient
import com.example.ban_giay_mobile.databinding.ActivityCartBinding
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Helper.ChangeNumberItemsListener
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity(), ChangeNumberItemsListener {
    private lateinit var binding: ActivityCartBinding
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
    private val cartItems = ArrayList<ItemModel>()
    private lateinit var cartAdapter: CartAdapter
    private var userId: Int = -1

    // Trong CartActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy userId từ SharedPreferences
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        userId = sharedPref.getInt("USER_ID", -1)

        // Lấy userId từ SharedPreferences
        userId = getUserId()

        // Log giá trị userId
        Log.d("CartActivity", "User ID: $userId")

        if (userId == -1) {
            // Handle trường hợp userId không tồn tại (người dùng chưa đăng nhập)
            finish()
            return
        }

        setupRecyclerView()
        loadCartFromServer()
        calculatePrices()

        val itemId = intent.getIntExtra("ITEM_ID", -1)
        if (itemId != -1) {
            loadItemDetails(itemId)
        }

        binding.backBtn.setOnClickListener {
            saveCartToServer()
            finish()
        }

        binding.checkoutBtn.setOnClickListener {
            val totalAmount = getTotalAmount()
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("totalAmount", totalAmount)

            // Chuyển thông tin giỏ hàng (cartItems) sang OrderDetailActivity
            intent.putParcelableArrayListExtra("cartItems", cartItems)

            startActivity(intent)
        }


    }

    private fun getTotalAmount(): Double {
        val totalAmountString = binding.totalTxt.text.toString()

        // Loại bỏ ký hiệu tiền tệ và khoảng trắng
        val cleanedAmountString = totalAmountString.replace("$", "").replace(",", "").trim()

        // Chuyển đổi chuỗi còn lại thành Double
        return cleanedAmountString.toDoubleOrNull() ?: 0.0
    }


    private fun getUserId(): Int {
        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        return sharedPref.getInt("USER_ID", -1) // Trả về -1 nếu không tìm thấy
    }


    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems, this, this)
        binding.viewCart.adapter = cartAdapter
        binding.viewCart.layoutManager = LinearLayoutManager(this)
    }

    private fun loadItemDetails(itemId: Int) {
        viewModel.loadItemById(itemId)
        viewModel.item.observe(this) { item ->
            if (item != null) {
                item.size = intent.getStringExtra("ITEM_SIZE")
                addItemToCart(item)
                calculatePrices()
            }
        }
    }

    private fun loadCartFromServer() {
        RetrofitClient.instance.getCart(userId).enqueue(object : Callback<List<ItemModel>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<ItemModel>>, response: Response<List<ItemModel>>) {
                if (response.isSuccessful) {
                    cartItems.clear()
                    response.body()?.let {
                        cartItems.addAll(it)
                        for (item in cartItems) {
                            Log.d("CartActivity", "Cart Item: id=${item.id}, title=${item.title}, size=${item.size}, quantity=${item.quantity}")
                        }
                    }
                    cartAdapter.notifyDataSetChanged()
                } else {
                    Log.e("CartActivity", "Failed to load cart items: ${response.errorBody()?.string()}")
                }
                updateEmptyTextVisibility()
                calculatePrices()
            }

            override fun onFailure(call: Call<List<ItemModel>>, t: Throwable) {
                // Handle failure
                Log.e("CartActivity", "Error loading cart items", t)
            }
        })
    }

    private fun saveCartToServer() {
        RetrofitClient.instance.saveCart(userId, cartItems).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle successful save
                } else {
                    Log.e("saveCartToServer", "Failed to save cart: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("saveCartToServer", "Error saving cart", t)
            }
        })
    }


    private fun addItemToCart(item: ItemModel) {
        val existingItemIndex = cartItems.indexOfFirst { it.id == item.id && it.size == item.size}
        Log.d("addItemToCart", "addItemToCart: $existingItemIndex ${item.id} ${item.size} ")
        if (existingItemIndex != -1) {
            cartItems[existingItemIndex].quantity += 1
            cartAdapter.notifyItemChanged(existingItemIndex)
        } else {
            item.quantity = 1
            cartItems.add(item)
            cartAdapter.notifyItemInserted(cartItems.size - 1)
        }
        saveCartToServer()
        updateEmptyTextVisibility()
        calculatePrices()
    }

    private fun updateEmptyTextVisibility() {
        if (cartItems.isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.viewCart.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.viewCart.visibility = View.VISIBLE
        }
    }

    override fun onItemCountChanged(position: Int, quantity: Int) {
        Log.d("CartActivity", "onItemCountChanged: position=$position, quantity=$quantity")
        if (quantity == 0) {
            cartItems.removeAt(position)
            cartAdapter.notifyItemRemoved(position)
        } else {
            cartItems[position].quantity = quantity
            cartAdapter.notifyItemChanged(position)
        }
        saveCartToServer()
        calculatePrices()
    }

    private fun calculatePrices() {
        var subtotal = 0.0
        for (item in cartItems) {
            subtotal += item.price * item.quantity
        }

        val tax = subtotal * 0.1 // Giả sử thuế là 10%
        val deliveryFee = 5.0 // Giả sử phí giao hàng cố định là 5.0
        // Nếu không có đơn hàng thì total = 0
        val total = if (subtotal > 0) subtotal + tax + deliveryFee else 0.0

        binding.totalFeeTxt.text = String.format("$%.2f", subtotal)
        binding.taxTxt.text = String.format("$%.2f", tax)
        binding.deliveryTxt.text = String.format("$%.2f", deliveryFee)
        binding.totalTxt.text = String.format("$%.2f", total)

        // Hiển thị thông báo nếu giỏ hàng rỗng
        binding.emptyTxt.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE
    }
}
