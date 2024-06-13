package com.example.sel.base.model


data class ResponseRank(
    var id: Int? = null,
    var max_score: Int? = null,
    var month: Int? = null,
    var user: User? = null,
    var user_id: Int? = null,
    var year: Int? = 0
)