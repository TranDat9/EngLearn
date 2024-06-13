package com.example.sel.interfaces

import androidx.annotation.LayoutRes

/**
 * OVERRIDE WHEN HAVE >1 TYPE OF MODELS OR HAVE LOAD MORE
 * viewType = 0 -->Loading item for load more
 *
 * MUST OVERRIDE:
 * layoutId = XML file for item layout (Model)
 *
 * MUST OVERRIDE IF HAVE LOAD MORE/DELETE:
 * areItemsTheSame --> Override with item === this
 * areContentsTheSame --> Override with other.id == this.id
 */

interface ItemBaseModel {
    @get:LayoutRes
    val layoutId: Int
    val viewType: Int
        get() = 1
    fun areItemsTheSame(other: ItemBaseModel): Boolean = false
    fun areContentsTheSame(other: ItemBaseModel): Boolean = false
}



