package com.example.ban_giay_mobile.repository

import com.example.ban_giay_mobile.Model.UserModel
import com.example.ban_giay_mobile.api.ApiService
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    suspend fun getUserById(userId: Int): Response<UserModel> {
        return apiService.getUserById(userId)
    }

    suspend fun updateUserById(userId: Int, userData: UserModel): Response<UserModel> {
        return apiService.updateUserById(userId, userData)
    }


}