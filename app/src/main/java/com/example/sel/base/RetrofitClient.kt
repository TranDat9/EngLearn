package com.example.sel.base

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.dictionaryapi.dev/"
    //private const val BASE_URL_AI = "https://api.openai.com/"
    //private const val API_KEY = "sk-proj-E7SZkZ2EQYgnUOyLjkLiT3BlbkFJjRnONsdExJCPmghmJokc"


//    private val authInterceptor = Interceptor { chain ->
//        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization", "Bearer $API_KEY")  // Sử dụng API_KEY đã khai báo
//            .build()
//        chain.proceed(newRequest)
//    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: DictionaryApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(DictionaryApi::class.java)
    }


}