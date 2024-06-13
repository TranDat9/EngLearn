package com.example.sel.base.model

import androidx.databinding.BaseObservable
import com.example.sel.R
import com.example.sel.interfaces.ItemBaseModel

data class ItemPostConvert (
    var id : Int?=null,
    var description : String?= null,
    var newImage : String?= null,
    @Transient var isCheckAnswer: Boolean? = false,
    private val onItemCheckToggle: (ItemPostConvert)->Unit = {},
    override val layoutId :Int = R.layout.item_choose_answer,
    override val viewType: Int = 0
) : ItemBaseModel, BaseObservable() {

    fun onItemCheckToggle() {
        onItemCheckToggle(this)
    }

}