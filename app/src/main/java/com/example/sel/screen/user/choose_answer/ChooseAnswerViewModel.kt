package com.example.sel.screen.user.choose_answer

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.BasicViewModel
import com.example.sel.base.model.Choose
import com.example.sel.base.model.ItemChooseAnswerConvert
import com.example.sel.base.model.ResponseHomeQuestion
import com.example.sel.base.model.ResponseQuestionQuiz
import com.example.sel.interfaces.ItemBaseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChooseAnswerViewModel (application: Application) : BasicViewModel(application) {
    val convertListQuestion = MutableLiveData<ResponseQuestionQuiz>().also {}
    val contentQuestion = MutableLiveData<String>().also {
        it.value = ""
    }
    val imgQuestion = MutableLiveData<String>().also {
        it.value = ""
    }
    val score = MutableLiveData<String>().also {
        it.value = "0"
    }
    fun getListQuestion(iD: Int?): ResponseQuestionQuiz? {
        viewModelScope.launch {
            try {
                delay(100)
                val response = ApiService.apiService.getAllQuestionHome(iD?:0)
                if (response.isSuccessful) {
                    val result = response.body()
                    convertListQuestion.postValue(result)

                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
    var onClickItemAnswer: (ItemChooseAnswerConvert) -> Unit = {}
    private var selectedItem: ItemChooseAnswerConvert? = null

    fun onItemCheck(item: ItemChooseAnswerConvert) {
        selectedItem?.isCheck = false // Bỏ chọn item cũ
        item.isCheck = true
        selectedItem = item
        onClickItemAnswer(item)
        convertedItemChooseAnswer.value = convertedItemChooseAnswer.value
    }

    val convertedItemChooseAnswer = MutableLiveData<List<ItemBaseModel>>().also {
        it.value = null
    }

    fun convertListItemAnswer(originalHomeMenuList: List<Choose>?) {
        val enableItemListItem = mutableListOf<ItemBaseModel>()
        originalHomeMenuList?.forEachIndexed { index, it ->
            val newHomeMenuItem = ItemChooseAnswerConvert(
                id = index,
                type = it.type,
                name = it.q,
                isCheck = false,
                onItemCheckToggle = ::onItemCheck,
                layoutId = R.layout.item_choose_answer,
                viewType = 0
            )
            enableItemListItem.add(newHomeMenuItem)
        }
        convertedItemChooseAnswer.postValue(enableItemListItem)
    }

}