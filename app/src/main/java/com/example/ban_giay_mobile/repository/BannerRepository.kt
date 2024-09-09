package com.example.ban_giay_mobile.repository

import com.example.ban_giay_mobile.Model.SliderModel
import com.example.ban_giay_mobile.api.ApiService
import retrofit2.Response

class BannerRepository(private val apiService: ApiService) {

    suspend fun getBanners(): Response<List<SliderModel>> {
        return apiService.getBanners()
    }
}
