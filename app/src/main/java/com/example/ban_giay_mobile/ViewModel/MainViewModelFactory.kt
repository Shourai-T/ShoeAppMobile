package com.example.ban_giay_mobile.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ban_giay_mobile.repository.BannerRepository
import com.example.ban_giay_mobile.ViewModel.MainViewModel
import com.example.ban_giay_mobile.repository.BrandRepository
import com.example.ban_giay_mobile.repository.FavouriteRepository
import com.example.ban_giay_mobile.repository.ItemRepository
import com.example.ban_giay_mobile.repository.OrderRepository
import com.example.ban_giay_mobile.repository.UserRepository

class MainViewModelFactory(
    private val bannerRepository: BannerRepository,
    private val brandRepository: BrandRepository,
    private val itemRepository: ItemRepository,
    private val favouriteRepository: FavouriteRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(bannerRepository, brandRepository, itemRepository, favouriteRepository, userRepository, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}