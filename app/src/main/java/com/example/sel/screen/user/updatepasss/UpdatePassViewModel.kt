package com.example.sel.screen.user.updatepasss

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.RequestRegister
import com.example.sel.base.model.RequestUpdatePass
import com.example.sel.base.model.ResponseMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UpdatePassViewModel(application: Application) : BasicViewModel(application) {

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess
    val updatePass = MutableLiveData<ResponseMessage>().also {  }


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    fun loadDataFromAPI(requestUpdatePass: RequestUpdatePass): ResponseMessage? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAPIUpdatePass(requestUpdatePass)
                if (response.isSuccessful) {
                    response.body()
                    _registerSuccess.postValue(true)
                    updatePass.postValue(response.body())
                } else {
                    _errorMessage.postValue(" VUI LÒNG THỬ LẠI")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("VUI LÒNG THỬ LẠI")
            }
        }
        return null
    }

}