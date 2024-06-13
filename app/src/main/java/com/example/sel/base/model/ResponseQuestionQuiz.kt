package com.example.sel.base.model

data class ResponseQuestionQuiz(
    val post: Post? = null,
    val questions: List<QuestionHome?>? = null,
    val topic_name: String?
)
data class Post(
    val created_at: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val new_question_id: String? = null,
    val question_id: Any? = null,
    val topic: Topic? = null,
    val topic_id: Int? = null,
    val updated_at: String? = null,
)
