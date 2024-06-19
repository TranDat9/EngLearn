package com.example.sel.base

import com.example.sel.base.model.DictionaryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    fun getWordMeaning(@Path("word") word: String): Call<List<DictionaryResponse>>
}