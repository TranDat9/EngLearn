package com.example.sel.screen.user.resultAnswer

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.RequestSubmitExams
import com.example.sel.base.model.ResponseQuestionQuiz
import com.example.sel.base.model.ResponseSubmitExams
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultAnswerViewModel  (application: Application) : BasicViewModel(application)  {
    var point = MediatorLiveData<String>().also {
        it.value = ""
    }
    val submitExams = MutableLiveData<ResponseSubmitExams>().also {}
    fun getApiSubmitExams(id : Int,requestSubmitExams: RequestSubmitExams): ResponseQuestionQuiz? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAPISubmitExams(id,requestSubmitExams)
                if (response.isSuccessful) {
                    val result = response.body()
                    submitExams.postValue(result)

                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
}