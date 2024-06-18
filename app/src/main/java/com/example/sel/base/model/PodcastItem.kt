package com.example.sel.base.model

data class PodcastItem(
    val id: Int?=null,
    val name: String?=null,
    val audioUrl: String?=null,
    val transcript: List<Transcript>,
    val created_at: String?=null,
    val updated_at: String?=null,
    val mane_podcast: String?=null,
    val image :String?=null,
)

data class Transcript(
    val startTime: Int,
    val endTime: Int,
    val text: String
)
