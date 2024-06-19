package com.example.sel.base.model


data class DictionaryResponse(
    val word: String,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetics>
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String?
)

data class Phonetics(
    val text: String?,
    val audio: String?
)

