package com.example.ban_giay_mobile.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ban_giay_mobile.Model.BrandModel
import com.example.ban_giay_mobile.Model.ItemModel
import com.example.ban_giay_mobile.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainViewModel (): ViewModel() {
    private val mysqlDatabase = MySQLDatabase.getInstance()
    private  val _banner = MutableLiveData<List<SliderModel>>()
    private val _brand = MutableLiveData<MutableList<BrandModel>>()
    private val _popular = MutableLiveData<MutableList<ItemModel>>()

    val  brands: LiveData<MutableList<BrandModel>> = _brand
    val  popular: LiveData<MutableList<ItemModel>> = _popular

    val banners: LiveData<List<SliderModel>> = _banner
    fun loadBanner() {
        val Ref = mysqlDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null)
                        lists.add(list)
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        }
    fun loadBrand() {
        val Ref = mysqlDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<BrandModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(BrandModel::class.java)
                    if (list != null)
                        lists.add(list)
                }
                _brand.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun loadPopular() {
        val Ref = mysqlDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemModel::class.java)
                    if (list != null)
                        lists.add(list)
                }
                _popular.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}