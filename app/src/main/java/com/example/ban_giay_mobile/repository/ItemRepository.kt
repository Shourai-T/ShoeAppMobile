package com.example.ban_giay_mobile.repository

import com.example.ban_giay_mobile.Model.ItemImagesModel
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Model.SizeModel
import com.example.ban_giay_mobile.api.ApiService
import retrofit2.Response

class ItemRepository(private val apiService: ApiService) {

    // Hàm để lấy danh sách sản phẩm
    suspend fun getItems(): Response<List<ItemModel>> {
        return apiService.getItems()
    }

    // Hàm để lấy thông tin sản phẩm theo ID
    suspend fun getItemById(itemId: Int): Response<ItemModel> {
        return apiService.getItemById(itemId)
    }

    suspend fun getItemSizes(id: Int): Response<List<SizeModel>> {
        return apiService.getItemSizes(id)
    }

    suspend fun getItemImages(id: Int): Response<List<ItemImagesModel>> {
        return apiService.getItemImages(id)
    }

    suspend fun getItemsByBrand(brandId: Int): Response<List<ItemModel>> {
        return apiService.getItemsByBrand(brandId)
    }

    suspend fun getItemsBySearch(query: String): Response<List<ItemModel>> {
        return apiService.getItemsBySearch(query)
    }

    suspend fun addItem(item: ItemModel): Response<ItemModel> {
        return apiService.addItem(item)
    }

    suspend fun updateItem(id: Int, item: ItemModel): Response<ItemModel> {
        return apiService.updateItem(id, item)
    }

    suspend fun deleteItem(id: Int): Response<Void> {
        return apiService.deleteItem(id)
    }


}
