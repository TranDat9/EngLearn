package com.example.sel.screen.user.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.ItemHomeMenuConverted
import com.example.sel.base.model.ItemPostConvert
import com.example.sel.base.model.PostID
import com.example.sel.base.model.ResponseHomeQuestion
import com.example.sel.base.model.ResponsePost
import com.example.sel.base.model.ResponseTopic
import com.example.sel.base.model.Topic
import com.example.sel.interfaces.ItemBaseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel (application: Application) : BasicViewModel(application) {


    val convertedHomeMenuList = MutableLiveData<ResponseTopic>().also {}

    fun loadDataFromAPI(): ResponseHomeQuestion? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAllTopic()
                if (response.isSuccessful) {
                    val result = response.body()
                    convertedHomeMenuList.postValue(result)

                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    var onClickHomeItemMenu: (ItemHomeMenuConverted) -> Unit = {}

    private fun onItemCheckToggle(item: ItemHomeMenuConverted) {
        onClickHomeItemMenu(item)
    }

    val convertedItemHomeMenuList = MutableLiveData<List<ItemBaseModel>>().also {
        it.value = null
    }

    fun convertListContractEnable(originalHomeMenuList: List<Topic>?) {
        val enableContractListToConvert = mutableListOf<ItemBaseModel>()
        originalHomeMenuList?.forEachIndexed { index, it ->
            val newHomeMenuItem = ItemHomeMenuConverted(
                id = it.id,
                name = it.name,
                type = "",
                image = it.newImage,
                description = "",
                onItemCheckToggle = ::onItemCheckToggle,
                layoutId = R.layout.item_new_transaction,
                viewType = 0
            )
            enableContractListToConvert.add(newHomeMenuItem)
        }

        convertedItemHomeMenuList.postValue(enableContractListToConvert)
    }

    ///// Post vào bottom sheet

    val convertedListPost = MutableLiveData<ResponsePost>().also {}
    fun loadDataFromAPIPost(Id : Int): ResponsePost? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAllPost(Id)
                if (response.isSuccessful) {
                    val result = response.body()
                    convertedListPost.postValue(result)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    val convertedItemPostList = MutableLiveData<List<ItemBaseModel>>().also {
        it.value = null
    }

    var onClickItemPost: (ItemPostConvert) -> Unit = {}

    var dismissDialog: ()->Unit = {}

    private fun onItemCheckToggle(item: ItemPostConvert) {
        onClickItemPost(item)
        dismissDialog()
    }
    fun convertListPost(originalHomeMenuList: List<PostID>?) {
        val enableTopicListToConvert = mutableListOf<ItemBaseModel>()
        originalHomeMenuList?.forEachIndexed { index, it ->
            val newHomeMenuItem = ItemPostConvert(
                id = it.id,
                description= it.name,
                onItemCheckToggle = ::onItemCheckToggle,
                layoutId = R.layout.item_topic,
                viewType = 0
            )
            enableTopicListToConvert.add(newHomeMenuItem)
        }

        convertedItemPostList.postValue(enableTopicListToConvert)
    }

}