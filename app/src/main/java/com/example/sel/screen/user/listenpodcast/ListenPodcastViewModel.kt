package com.example.sel.screen.user.listenpodcast

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.ItemPodCastConverted
import com.example.sel.base.model.PodcastItem
import com.example.sel.base.model.ResponsePodcastId
import com.example.sel.interfaces.ItemBaseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListenPodcastViewModel(application: Application)  : BasicViewModel(application) {
     val reponseItemPodcast = MutableLiveData<ResponsePodcastId>().also {  }
    private val _errorMessage = MutableLiveData<String>()


    fun loadIdPodcast(id : Int) : ResponsePodcastId?
    {
        viewModelScope.launch {
            try {
                delay(100)
                val reponse = ApiService.apiService.getPodCastId(id)
                if(reponse.isSuccessful)
                {
                    val result = reponse.body()
                    reponseItemPodcast.postValue(result)
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


}
