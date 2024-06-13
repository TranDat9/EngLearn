package com.example.sel.base.model


data class ResponseHomeQuestion(
    val questions: List<QuestionHome>?= null,
)

data class QuestionHome(
    val answer: String?= null,
    val choose: List<Choose>?= null,
    val created_at: String?= null,
    val id: Int?= null,
    val options: String?= null,
    val point: Int?= null,
    val question: String?= null,
    val newImage: String?= null,
    val updated_at: String?= null,
)

data class Choose(
    var q: String? = null,
    var type: String? = null
)
