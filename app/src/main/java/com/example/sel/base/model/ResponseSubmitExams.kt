package com.example.sel.base.model

data class ResponseSubmitExams(
    var HistoryExams: HistoryExams? = null
)

data class HistoryExams(
    var correct_answer: Int? = null,
    var created_at: String? = null,
    var fail_answer: Int? = null,
    var id: Int? = null,
    var question_check: List<QuestionCheck?>? = listOf(),
    var score: Int? = null,
    var updated_at: String? = null,
    var user_id: Int? = null,
)