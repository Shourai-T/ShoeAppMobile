package com.example.ban_giay_mobile.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ban_giay_mobile.Model.SliderModel

class MainViewModel (): ViewModel() {
    private val mysqlDatabase = MySQLDatabase.getInstance()
    private  val _banner = MutableLiveData<List<SliderModel>>()

    val banner: LiveData<List<SliderModel>> = _banner
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
            }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}