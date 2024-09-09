package com.example.ban_giay_mobile.Model

import com.google.gson.annotations.SerializedName

data class OrderModel(
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,
    @SerializedName("total_price") val totalPrice: Double,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("orderDetails") val orderDetails: List<OrderDetailModel>,
    val userId: Int
)


