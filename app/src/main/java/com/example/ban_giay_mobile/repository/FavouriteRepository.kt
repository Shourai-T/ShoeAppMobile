package com.example.ban_giay_mobile.repository

import com.example.ban_giay_mobile.Model.FavouriteModel
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.api.ApiService
import retrofit2.Call
import retrofit2.Response

class FavouriteRepository(private val apiService: ApiService) {

    // Create a FavouriteRequest object to match the API call
    suspend fun addFavourite(userId: Int, itemId: Int): Response<FavouriteModel> {
        val favouriteRequest = ApiService.FavouriteRequest(userId, itemId)
        return apiService.addFavourite(favouriteRequest)
    }

    suspend fun getFavourite(userId: Int, itemId: Int): Response<FavouriteModel> {
        return apiService.getFavourite(userId, itemId)
    }

    suspend fun removeFavourite(userId: Int, itemId: Int): Response<Unit> {
        return apiService.removeFavourite(userId, itemId)
    }

    suspend fun getFavourites(userId: Int): Response<List<FavouriteModel>> {
        return apiService.getFavourites(userId)
    }
}

