package com.example.ban_giay_mobile.Model

import com.google.gson.annotations.SerializedName

data class FavouriteModel(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("item_id") val itemId: Int
)