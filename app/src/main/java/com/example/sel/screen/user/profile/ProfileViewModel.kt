package com.example.sel.screen.user.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.RequesPasword
import com.example.sel.base.model.ResponseHomeQuestion
import com.example.sel.base.model.ResponseTopic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel (application: Application) : BasicViewModel(application){

    private val _updaterSuccess = MutableLiveData<Boolean>()
    val updaterSuccess: LiveData<Boolean> = _updaterSuccess

    private val _updaterFail= MutableLiveData<String>()
    val updaterFail: LiveData<String> = _updaterFail
    fun loadDataFromAPI(id: Int, input: RequesPasword): Any? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAPIUpdateProfile(id, input)
                if (response.isSuccessful) {
                    val result = response.body()
                    _updaterSuccess.postValue(true)

                } else {
                    _updaterFail.postValue("CẬP NHẬT TÀI KHOẢN KHÔNG THÀNH CÔNG")
                }
            } catch (e: Exception) {
                _updaterFail.postValue("CẬP NHẬT TÀI KHOẢN KHÔNG THÀNH CÔNG")

            }
        }
        return null
    }
}