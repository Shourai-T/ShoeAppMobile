package com.example.ban_giay_mobile.repository

import com.example.ban_giay_mobile.Model.BrandModel
import com.example.ban_giay_mobile.api.ApiService
import retrofit2.Response

class BrandRepository(private val apiService: ApiService) {

    suspend fun getBrands(): Response<List<BrandModel>> {
        return apiService.getBrands()
    }
}
