package com.example.sel.base.model

import com.example.sel.R
import com.example.sel.interfaces.ItemBaseModel


data class ItemHomeMenuConverted (
    var id : Int?=null,
    var name : String?= null,
    var type : String?= null,
    var image : String?= null,
    var description : String?= null,
    @Transient var isCheckAnswer: Boolean? = false,
    private val onItemCheckToggle: (ItemHomeMenuConverted)->Unit = {},
    override val layoutId :Int = R.layout.item_new_transaction,
    override val viewType: Int = 0
): ItemBaseModel {
    fun onItemCheckToggle() {
        onItemCheckToggle(this)
    }

}
