package com.example.sel.screen.user.podcast2

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.HistoryExam
import com.example.sel.base.model.ItemHistoryExamConvert
import com.example.sel.base.model.ItemPodCastConverted
import com.example.sel.base.model.ItemPostConvert
import com.example.sel.base.model.PodcastItem
import com.example.sel.base.model.ReponHistoryExam
import com.example.sel.base.model.ReponsePodcast
import com.example.sel.interfaces.ItemBaseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Podcast2ViewModel (application: Application) : BasicViewModel(application) {

    val reponHistoryExam = MutableLiveData<ReponsePodcast>().also {};
    private val _errorMessage = MutableLiveData<String>()

    fun loadDataPCFromApi() : ReponsePodcast?
    {
        viewModelScope.launch {
            try {
                delay(100)
                val reponse = ApiService.apiService.getAllPostCast()
                if(reponse.isSuccessful)
                {
                    //get list exam
                    val result = reponse.body()
                    reponHistoryExam.postValue(result)
                }else{
                    _errorMessage.postValue("loi")
                }
            }catch ( e: Exception)
            {
                _errorMessage.postValue("loi")
            }

        }
        return null
    }

    var onClickItemPocast: (ItemPodCastConverted) -> Unit = {}

    private fun onItemCheckToggle(item: ItemPodCastConverted) {
        onClickItemPocast(item)

    }

    val convertedListHistoryExam = MutableLiveData<List<ItemBaseModel>>().also {
        it.value= null
    }

    fun convertListContractEnable(originalHisExamList: List<PodcastItem>?) {
        val enableContractListToConvert = mutableListOf<ItemBaseModel>()
        originalHisExamList?.forEachIndexed { index, it ->
            val newHomeMenuItem = ItemPodCastConverted(
                id = it.id,
                mane_podcast = it.mane_podcast,
                image = it.image ,
                onItemCheckToggle = ::onItemCheckToggle,
                layoutId = R.layout.item_podcast2,
                viewType = 0
            )
            enableContractListToConvert.add(newHomeMenuItem)
        }

        convertedListHistoryExam.postValue(enableContractListToConvert)
    }
     



}