package com.example.sel.base.model

data class ResponseTopic(
    val topics: List<Topic>? = null
)

data class Topic(
    val created_at: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val updated_at: String? = null,
    val newImage : String? = null,
)