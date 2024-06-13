package com.example.sel.screen.user.ranking

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.ItemHomeMenuConverted
import com.example.sel.base.model.ItemRankConverted
import com.example.sel.base.model.ResponseHomeQuestion
import com.example.sel.base.model.ResponseRank
import com.example.sel.base.model.ResponseTopic
import com.example.sel.base.model.Topic
import com.example.sel.base.model.User
import com.example.sel.interfaces.ItemBaseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RankingViewModel (application: Application) : BasicViewModel(application) {

    val responseRank = MutableLiveData<List<ResponseRank>>()
    val topScore1 = MutableLiveData<String>()
    val topScore2 = MutableLiveData<String>()
    val topScore3 = MutableLiveData<String>()
    fun loadDataFromAPI(): ResponseHomeQuestion? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAPIRankMonth()
                if (response.isSuccessful) {
                     response.body()?.let {
                        responseRank.postValue(it)
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        return null
    }


    val convertedItemRank = MutableLiveData<List<ItemBaseModel>>().also {
        it.value = null
    }

    fun convertListContractEnable(originalList: List<ResponseRank>?) {
        val enableContractListToConvert = mutableListOf<ItemBaseModel>()
        originalList?.forEachIndexed { index, it ->
            val newHomeMenuItem = ItemRankConverted(
                id = index,
                name = it.user?.name,
                email = it.user?.email,
                point = it.max_score.toString(),
                layoutId = R.layout.item_ranking,
                viewType = 0
            )
            enableContractListToConvert.add(newHomeMenuItem)
        }

        convertedItemRank.postValue(enableContractListToConvert)
    }



}