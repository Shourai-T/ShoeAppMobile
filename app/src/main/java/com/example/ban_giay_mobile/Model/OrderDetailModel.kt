package com.example.ban_giay_mobile.Model

import com.google.gson.annotations.SerializedName

data class OrderDetailModel(
    @SerializedName("detail_id") val detailId: Int,
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("product_name") val productName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("size") val size: String
)