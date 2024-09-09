package com.example.ban_giay_mobile.Model

data class UserModel(
    val id: Int,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val created_at: String? = null,
    val phone: String?,
    val address: String?,
    val firstname: String?,
)