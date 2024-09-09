package com.example.ban_giay_mobile.api

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
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("v0/banners")
    suspend fun getBanners(): Response<List<SliderModel>>

    @GET("v0/category")
    suspend fun getBrands() :Response<List<BrandModel>>

    data class FavouriteRequest(
        @SerializedName("user_id") val userId: Int,
        @SerializedName("item_id") val itemId: Int
    )


    // Thêm sản phẩm vào danh sách yêu thích
    @POST("v0/favourites")
    suspend fun addFavourite(
        @Body favouriteRequest: FavouriteRequest
    ): Response<FavouriteModel>

    // Xóa sản phẩm khỏi danh sách yêu thích
    @DELETE("v0/favourites/{userId}/{itemId}")
    suspend fun removeFavourite(
        @Path("userId") userId: Int,
        @Path("itemId") itemId: Int
    ): Response<Unit>

    // Lấy danh sách sản phẩm yêu thích của người dùng
    @GET("v0/favourites/{userId}")
    suspend fun getFavourites(
        @Path("userId") userId: Int
    ): Response<List<FavouriteModel>>

    @GET("v0/favourites/{userId}/{itemId}")
    suspend fun getFavourite(
        @Path("userId") userId: Int,
        @Path("itemId") itemId: Int
    ): Response<FavouriteModel>

    @GET("v0/items")
    suspend fun getItems(): Response<List<ItemModel>>

    @GET("v0/items/{id}")
    suspend fun getItemById(@Path("id") id: Int): Response<ItemModel>

    @GET("v0/items/{id}/sizes")
    suspend fun getItemSizes(@Path("id") id: Int): Response<List<SizeModel>>

    @GET("v0/items/{id}/images")
    suspend fun getItemImages(@Path("id") id: Int): Response<List<ItemImagesModel>>

    @GET("v0/items/brand/{brandId}")
    suspend fun getItemsByBrand(
        @Path("brandId") brandId: Int
    ): Response<List<ItemModel>>

    @GET("v0/items/search")
    suspend fun getItemsBySearch(
        @Query("query") query: String
    ): Response<List<ItemModel>>

    @POST("v0/items/add")
    suspend fun addItem(
        @Body item: ItemModel
    ): Response<ItemModel>

    @PUT("v0/items/{id}")
    suspend fun updateItem(
        @Path("id") id: Int,
        @Body item: ItemModel
    ): Response<ItemModel>

    @DELETE("v0/items/{id}")
    suspend fun deleteItem(
        @Path("id") id: Int
    ): Response<Void>


    @POST("v0/cart/{userId}")
    fun saveCart(
        @Path("userId") userId: Int,
        @Body cartItems: List<ItemModel>
    ): Call<Void>

    @GET("v0/cart/{userId}")
    fun getCart(
        @Path("userId") userId: Int
    ): Call<List<ItemModel>>

    @POST("v0/users/register")
    suspend fun registerUser(@Body user: UserModel): Response<Void>

    data class LoginResponse(
        val message: String,
        val user: UserModel
    )

    @PUT("v0/users/{id}")
    suspend fun updateUserById(
        @Path("id") userId: Int,
        @Body userData: UserModel
    ): Response<UserModel>

    @POST("v0/users/login")
    suspend fun loginUser(@Body user: UserModel): Response<LoginResponse>

    @GET("v0/users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<UserModel>

    // Tạo đơn hàng mới cùng với chi tiết đơn hàng
    @POST("v0/orders")
    suspend fun createOrder(
        @Body order: OrderModel
    ): Response<OrderModel>

    // Lấy thông tin đơn hàng và chi tiết đơn hàng theo ID
    @GET("v0/orders/{orderId}")
    suspend fun getOrderById(
        @Path("orderId") orderId: Int
    ): Response<OrderModel>

    // Lấy danh sách tất cả các đơn hàng
    @GET("v0/orders")
    suspend fun getAllOrders(): Response<List<OrderModel>>

    // Xóa đơn hàng theo ID (sẽ xóa luôn chi tiết đơn hàng)
    @DELETE("v0/orders/{orderId}")
    suspend fun deleteOrder(
        @Path("orderId") orderId: Int
    ): Response<Void>

    // Lấy chi tiết đơn hàng theo mã đơn hàng
    @GET("v0/orders/{orderId}/details")
    suspend fun getOrderDetailsByOrderId(
        @Path("orderId") orderId: Int
    ): Response<List<OrderDetailModel>>

    @POST("v0/users/admin/login")
    suspend fun loginAdmin(@Body admin: AdminModel): Response<LoginAdminResponse>

    data class LoginAdminResponse(
        val message: String,
        val admin: AdminModel
    )
}
