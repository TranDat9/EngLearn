package com.example.sel.base.model

import com.example.sel.R
import com.example.sel.interfaces.ItemBaseModel

data class ItemHistoryExamConvert (
    var id : Int?=null,
    var name_user : String?= null,
    var date : String?= null,
    var score : String?= null,

    private val onItemCheckToggle: (ItemHistoryExamConvert)->Unit = {},
    override val layoutId :Int = R.layout.item_historyexam,
    override val viewType: Int = 0
) : ItemBaseModel {
    fun onItemCheckToggle() {
        onItemCheckToggle(this)
    }

}