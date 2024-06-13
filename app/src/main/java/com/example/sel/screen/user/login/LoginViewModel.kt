package com.example.sel.screen.user.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.RequestLogin
import com.example.sel.base.model.ResponseLogin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel (application: Application) : BasicViewModel(application) {


    val loginData = MutableLiveData<ResponseLogin>().also {}

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String> = _errorMessage

    val submitExams = MutableLiveData<ResponseLogin>().also {}
    fun loadDataFromAPI(requestLogin: RequestLogin): ResponseLogin? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAPILogin(requestLogin)
                if (response.isSuccessful) {
                   val result = response.body()
                    loginData.postValue(result)
                    submitExams.postValue(result)
                } else {
                    _errorMessage.postValue("Login failed : VUI LÒNG KIỂM TRA LẠI TÀI KHOẢN MẬT KHẨU")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Login failed : VUI LÒNG KIỂM TRA LẠI TÀI KHOẢN MẬT KHẨU")
            }
        }
        return null
    }
}
