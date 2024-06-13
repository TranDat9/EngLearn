package com.example.sel.base.model

data class HistoryExam(
    val id: Int,
    val user_id: Int,
    val correct_answer: Int,
    val fail_answer: Int,
    val score: Int,
    val question_check: List<QuestionCheck>
)

