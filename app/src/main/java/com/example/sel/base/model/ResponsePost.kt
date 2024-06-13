package com.example.sel.base.model

data class ResponsePost(
    var topic: TopicID? = null,
)

data class TopicID(
    var created_at: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var topic_id: List<PostID>? = null,
    var updated_at: String? = ""
)

data class PostID(
    var created_at: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var new_question_id: String? = null,
    var question_id: Int? = null,
    var topic_id: Int? = null,
    var updated_at: String? = null
)