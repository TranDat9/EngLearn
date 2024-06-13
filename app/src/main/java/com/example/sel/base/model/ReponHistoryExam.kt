package com.example.sel.base.model


data class ReponHistoryExam(
     val historyExams :List<HistoryExam>?=null
)

data class HistoryExam(
    val id: Int?=null,
    val user_id: Int?=null,
    val correct_answer: Int?=null,
    val fail_answer: Int?=null,
    val score: String?=null,
    val question_check: List<QuestionCheck>?=null,
    val name_user: String? = null,
    val created_at: String? = null,
)


