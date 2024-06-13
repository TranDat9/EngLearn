package com.example.sel.base.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnsweredQuestion(
    val id : Int ? = null,
    val question: String? = null,
    val image: String? = null,
    val newImage : String ? =null,
    val options: String? = null,
    val answer: String? = null,
    val point: Int? = null,
    val user_choose: String? = null,
    val isCorrect: Boolean ? = null
): Parcelable