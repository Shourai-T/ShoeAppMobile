package com.example.ban_giay_mobile.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemModel(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var price: Int = 0,
    var rating: Double = 0.0,
    var picUrl: String = "",
    var brandId: Int = 0,
    var quantity: Int = 0,
    var size: String? = null
) : Parcelable
