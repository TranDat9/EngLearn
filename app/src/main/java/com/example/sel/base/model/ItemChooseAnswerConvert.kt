package com.example.sel.base.model

import com.example.sel.R
import com.example.sel.interfaces.ItemBaseModel

class ItemChooseAnswerConvert(
    var id: Int? = null,
    var name: String? = null,
    var type: String? = null,
    var image: String? = null,
    var description: String? = null,
    var isCheck :Boolean = false,
    private val onItemCheckToggle: (ItemChooseAnswerConvert) -> Unit = {},
    override val layoutId: Int = R.layout.item_choose_answer,
    override val viewType: Int = 0
) : ItemBaseModel {
    fun onItemCheckToggle() {
        onItemCheckToggle(this)
    }
}