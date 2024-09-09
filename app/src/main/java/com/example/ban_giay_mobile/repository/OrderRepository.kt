package com.example.ban_giay_mobile.repository

import com.example.ban_giay_mobile.Model.OrderModel
import com.example.ban_giay_mobile.Model.OrderDetailModel
import com.example.ban_giay_mobile.api.ApiService
import retrofit2.Response

class OrderRepository(private val apiService: ApiService) {

    suspend fun createOrder(order: OrderModel): Response<OrderModel> {
        return apiService.createOrder(order)
    }

    suspend fun getOrderById(orderId: Int): Response<OrderModel> {
        return apiService.getOrderById(orderId)
    }

    suspend fun getAllOrders(): Response<List<OrderModel>> {
        return apiService.getAllOrders()
    }

    suspend fun deleteOrder(orderId: Int): Response<Void> {
        return apiService.deleteOrder(orderId)
    }

    suspend fun getOrderDetailsByOrderId(orderId: Int): Response<List<OrderDetailModel>> {
        return apiService.getOrderDetailsByOrderId(orderId)
    }
}
