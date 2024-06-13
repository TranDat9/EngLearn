package com.example.sel.base.model

import com.example.sel.R
import com.example.sel.interfaces.ItemBaseModel


data class ItemRankConverted (
    var id : Int?=null,
    var name : String?= null,
    var email : String?= null,
    var point : String?= null,
    override val layoutId :Int = R.layout.item_ranking,
    override val viewType: Int = 0
): ItemBaseModel
