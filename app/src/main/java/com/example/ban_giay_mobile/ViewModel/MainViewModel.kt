package com.example.ban_giay_mobile.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ban_giay_mobile.Model.AdminModel
import com.example.ban_giay_mobile.Model.BrandModel
import com.example.ban_giay_mobile.Model.FavouriteModel
import com.example.ban_giay_mobile.Model.ItemImagesModel
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Model.OrderDetailModel
import com.example.ban_giay_mobile.Model.OrderModel
import com.example.ban_giay_mobile.Model.SizeModel
import com.example.ban_giay_mobile.Model.SliderModel
import com.example.ban_giay_mobile.Model.UserModel
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    private val bannerRepository: BannerRepository,
    private val brandRepository: BrandRepository,
    private val itemRepository: ItemRepository,
    private val favouriteRepository: FavouriteRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Banners
    private val _banners = MutableLiveData<List<SliderModel>>()
    val banners: LiveData<List<SliderModel>> get() = _banners

    // Brands
    private val _brands = MutableLiveData<List<BrandModel>>()
    val brands: LiveData<List<BrandModel>> get() = _brands

    // Items
    private val _items = MutableLiveData<List<ItemModel>>()
    val items: LiveData<List<ItemModel>> get() = _items

    private val _item = MutableLiveData<ItemModel>()
    val item: LiveData<ItemModel> get() = _item

    private val _itemImages = MutableLiveData<List<ItemImagesModel>>()
    val itemImages: LiveData<List<ItemImagesModel>> get() = _itemImages

    private val _itemSizes = MutableLiveData<List<SizeModel>>()
    val itemSizes: LiveData<List<SizeModel>> get() = _itemSizes

    private val _favourites = MutableLiveData<List<FavouriteModel>>()
    val favourites: LiveData<List<FavouriteModel>> get() = _favourites

    private val _favouriteAdded = MutableLiveData<Response<FavouriteModel>>()
    val favouriteAdded: LiveData<Response<FavouriteModel>> get() = _favouriteAdded

    private val _isFavourited = MutableLiveData<Boolean>()
    val isFavourited: LiveData<Boolean> get() = _isFavourited

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    private val _updateUserResult = MutableLiveData<Boolean>()
    val updateUserResult: LiveData<Boolean> get() = _updateUserResult

    // Order
    private val _order = MutableLiveData<OrderModel>()
    val order: LiveData<OrderModel> get() = _order

    private val _orders = MutableLiveData<List<OrderModel>>()
    val orders: LiveData<List<OrderModel>> get() = _orders

    private val _orderDetails = MutableLiveData<List<OrderDetailModel>>()
    val orderDetails: LiveData<List<OrderDetailModel>> get() = _orderDetails

    private val _createOrderResult = MutableLiveData<Response<OrderModel>>()
    val createOrderResult: LiveData<Response<OrderModel>> get() = _createOrderResult

    private val _deleteOrderResult = MutableLiveData<Response<Void>>()
    val deleteOrderResult: LiveData<Response<Void>> get() = _deleteOrderResult


    // Error
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadBanners() {
        viewModelScope.launch {
            try {
                val response = bannerRepository.getBanners()
                if (response.isSuccessful) {
                    _banners.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun loadBrands() {
        viewModelScope.launch {
            try {
                val response = brandRepository.getBrands()
                if (response.isSuccessful) {
                    _brands.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    // Load Items
    fun loadItems() {
        viewModelScope.launch {
            try {
                val response = itemRepository.getItems()
                if (response.isSuccessful) {
                    _items.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    // Load Item by ID
    fun loadItemById(id: Int) {
        viewModelScope.launch {
            try {
                val response = itemRepository.getItemById(id)
                if (response.isSuccessful) {
                    _item.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    // Load Item Sizes
    fun loadItemSizes(id: Int) {
        viewModelScope.launch {
            try {
                val response = itemRepository.getItemSizes(id)
                if (response.isSuccessful) {
                    _itemSizes.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun loadItemImages(id: Int) {
        viewModelScope.launch {
            try {
                val response = itemRepository.getItemImages(id)
                if (response.isSuccessful) {
                    _itemImages.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun loadItemsByBrand(brandId: Int) {
        viewModelScope.launch {
            try {
                val response = itemRepository.getItemsByBrand(brandId)
                if (response.isSuccessful) {
                    _items.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun loadItemsBySearch(query: String) {
        viewModelScope.launch {
            try {
                val response = itemRepository.getItemsBySearch(query)
                if (response.isSuccessful) {
                    _items.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    // Add item to favourites
    fun addFavourite(userId: Int, itemId: Int) {
        viewModelScope.launch {
            try {
                val response = favouriteRepository.addFavourite(userId, itemId)
                if (response.isSuccessful) {
                    _favouriteAdded.postValue(response) // Cập nhật LiveData
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun checkIfItemIsFavourited(userId: Int, itemId: Int) {
        viewModelScope.launch {
            try {
                val response = favouriteRepository.getFavourite(userId, itemId)
                Log.d("API Response", "Code: ${response.code()}, Message: ${response.message()}")
                if (response.isSuccessful) {
                    val favourite = response.body()
                    Log.d("Body Response", "loadFavourites: ${response.body()} ")
                    val isFavourited = favourite != null
                    _isFavourited.postValue(isFavourited)
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }



    // Remove item from favourites
    fun removeFavourite(userId: Int, itemId: Int) {
        viewModelScope.launch {
            try {
                val response = favouriteRepository.removeFavourite(userId, itemId)
                if (response.isSuccessful) {
                    loadFavourites(userId) // Refresh the list
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun loadFavourites(userId: Int) {
        viewModelScope.launch {
            try {
                val response = favouriteRepository.getFavourites(userId)
                Log.d("API Response", "Code: ${response.code()}, Message: ${response.message()}")
                if (response.isSuccessful) {
                    _favourites.postValue(response.body())
                    Log.d("Body Response", "loadFavourites: ${response.body()} ")
                    // Sau khi nhận được danh sách yêu thích, lấy thông tin chi tiết của các sản phẩm
                    val itemIds = response.body()?.map { it.itemId } ?: emptyList()
                    Log.d("ItemIds", "ItemsIds $itemIds ")
                    loadItemsDetails(itemIds)
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun loadItemsDetails(itemIds: List<Int>) {
        viewModelScope.launch {
            val items = mutableListOf<ItemModel>()
            itemIds.distinct().forEach { id -> // Ensure each ID is unique
                try {
                    val response = itemRepository.getItemById(id)
                    Log.d("Item API Response", "Code: ${response.code()}, Message: ${response.message()}")
                    if (response.isSuccessful) {
                        response.body()?.let { items.add(it) }
                    } else {
                        _error.postValue("Error: ${response.code()} - ${response.message()}")
                    }
                } catch (e: Exception) {
                    _error.postValue("Exception: ${e.message}")
                }
            }
            _items.postValue(items)
        }
    }

    fun loadUserById(userId: Int) {
        viewModelScope.launch {
            try {
                val response = userRepository.getUserById(userId)
                if (response.isSuccessful) {
                    _user.postValue(response.body())
                    Log.d("loadUserById", "loadUserById: ${response.body()}")
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun updateUserById(userId: Int, userData: UserModel) {
        viewModelScope.launch {
            try {
                val response = userRepository.updateUserById(userId, userData)
                Log.d("updateUserById", "API Response: $userData")
                if (response.isSuccessful) {
                    _user.value = response.body()
                    Log.d("updateUserById", "Update successful: ${response.body()}")
                    _updateUserResult.value = true
                } else {
                    Log.e("updateUserById", "Update failed: ${response.errorBody()}")
                    _updateUserResult.value = false
                }
            } catch (e: Exception) {
                Log.e("updateUserById", "Exception: ${e.message}")
                _updateUserResult.value = false
            }
        }
    }

    fun createOrder(order: OrderModel) {
        viewModelScope.launch {
            try {
                val response = orderRepository.createOrder(order)
                _createOrderResult.postValue(response)
                if (!response.isSuccessful) {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun getOrderById(orderId: Int) {
        viewModelScope.launch {
            try {
                val response = orderRepository.getOrderById(orderId)
                if (response.isSuccessful) {
                    _order.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun getAllOrders() {
        viewModelScope.launch {
            try {
                val response = orderRepository.getAllOrders()
                if (response.isSuccessful) {
                    _orders.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                val response = orderRepository.deleteOrder(orderId)
                _deleteOrderResult.postValue(response)
                if (!response.isSuccessful) {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    fun getOrderDetailsByOrderId(orderId: Int) {
        viewModelScope.launch {
            try {
                val response = orderRepository.getOrderDetailsByOrderId(orderId)
                if (response.isSuccessful) {
                    _orderDetails.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    // Thêm item
    fun addItem(item: ItemModel) {
        viewModelScope.launch {
            try {
                val response = itemRepository.addItem(item)
                if (response.isSuccessful) {
                    // Cập nhật LiveData nếu cần
                    _item.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    // Cập nhật item
    fun updateItem(itemId: Int, item: ItemModel) {
        viewModelScope.launch {
            try {
                val response = itemRepository.updateItem(itemId, item)
                if (response.isSuccessful) {
                    // Cập nhật LiveData nếu cần
                    _item.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

    // Xóa item
    fun deleteItem(itemId: Int) {
        viewModelScope.launch {
            try {
                val response = itemRepository.deleteItem(itemId)
                if (response.isSuccessful) {
                    // Cập nhật LiveData nếu cần
                    // Ví dụ: refresh danh sách items
                    loadItems()
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Exception: ${e.message}")
            }
        }
    }

}
