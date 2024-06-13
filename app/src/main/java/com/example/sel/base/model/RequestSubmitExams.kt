package com.example.sel.base.model

data class RequestSubmitExams(
    var question_check: List<AnsweredQuestion>? = null,
    var user_id: Int? = null,
    var score: Int? = null,
    var fail_answer: Int? = null,
    var correct_answer: Int? = null,
    var name_user: String? = null,
)

data class QuestionCheck(
    var answer: String? = null,
    var id: Int? = null,
    var image: String? = null,
    var isCorrect: Boolean? = false,
    var options: String? = null,
    var point: Int? = null,
    var question: String? = null,
    var user_choose: String? = null
)