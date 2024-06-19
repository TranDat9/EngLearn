package com.example.sel.screen.user.historyexam

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.HistoryExam
import com.example.sel.base.model.ItemHistoryExamConvert
import com.example.sel.base.model.ItemHomeMenuConverted
import com.example.sel.base.model.ReponHistoryExam
import com.example.sel.base.model.Topic
import com.example.sel.interfaces.ItemBaseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryExamViewModel(application: Application) : BasicViewModel(application) {

    val reponHistoryExam = MutableLiveData<ReponHistoryExam>().also {};
    private val _errorMessage = MutableLiveData<String>()

    fun loadDataExamFromApi(id : Int?) : ReponHistoryExam?
    {
        viewModelScope.launch {
            try {
                delay(100)
                val reponse = ApiService.apiService.getHistoryByUser(id?:0)
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

    var onClickHomeItemMenu: (ItemHistoryExamConvert) -> Unit = {}

    private fun onItemCheckToggle(item: ItemHistoryExamConvert) {
        onClickHomeItemMenu(item)

    }

    val convertedListHistoryExam = MutableLiveData<List<ItemBaseModel>>().also {
        it.value= null
    }

    fun convertListContractEnable(originalHisExamList: List<HistoryExam>?) {
        val enableContractListToConvert = mutableListOf<ItemBaseModel>()
        originalHisExamList?.forEachIndexed { index, it ->
            val newHomeMenuItem = ItemHistoryExamConvert(
                id = it.id,
                name_user = it.name_user,
               date = it.created_at ,
                score = it.score,
                question_check=it.question_check,
                onItemCheckToggle = ::onItemCheckToggle,
                layoutId = R.layout.item_historyexam,
                viewType = 0
            )
            enableContractListToConvert.add(newHomeMenuItem)
        }

        convertedListHistoryExam.postValue(enableContractListToConvert)
    }


}