package com.example.ban_giay_mobile.Model

data class ItemModel(
    var title: String = "",
    var description: String = "",
    var picUrl:ArrayList<String> = ArrayList(),
    var size:ArrayList<String> = ArrayList(),
    var price: Int = 0,
    var rating: Double = 0.0,
    var numverInCart: Int = 0
)
