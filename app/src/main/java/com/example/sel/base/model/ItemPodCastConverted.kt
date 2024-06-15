package com.example.sel.base.model

import com.example.sel.R
import com.example.sel.interfaces.ItemBaseModel

data class ItemPodCastConverted (
    var id : Int?=null,
    var mane_podcast : String?= null,
    var image : String?= null,

    private val onItemCheckToggle: (ItemPodCastConverted)->Unit = {},
    override val layoutId :Int = R.layout.item_podcast2,
    override val viewType: Int = 0
): ItemBaseModel {
    fun onItemCheckToggle() {
        onItemCheckToggle(this)
    }

}

