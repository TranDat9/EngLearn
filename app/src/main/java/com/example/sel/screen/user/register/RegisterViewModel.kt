package com.example.sel.screen.user.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.RequestRegister
import com.example.sel.base.model.ResponseRegister
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel (application: Application) : BasicViewModel(application) {

    val registerSuccess = MutableLiveData<ResponseRegister>().also {  }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    fun loadDataFromAPI(requestRegister: RequestRegister): Any? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAPIRegister(requestRegister)
                if (response.isSuccessful) {
                    val result = response.body()
                    registerSuccess.postValue(result)
                } else {
                    _errorMessage.postValue("Login failed : VUI LÒNG ĐĂNG KÍ LẠI TÀI KHOẢN")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Login failed : VUI LÒNG ĐĂNG KÍ LẠI TÀI KHOẢN")
            }
        }
        return null
    }
}