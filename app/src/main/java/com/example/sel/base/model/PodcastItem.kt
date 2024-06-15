package com.example.sel.base.model

data class PodcastItem(
    val id: Int?=null,
    val name: String?=null,
    val audioUrl: String?=null,
    val transcript: List<Transcript>?=null,
    val created_at: String?=null,
    val updated_at: String?=null,
    val mane_podcast: String?=null,
    val image :String?=null,
)

data class Transcript(
    val startTime: Int?=null,
    val endTime: Int?=null,
    val text: String?=null
)
