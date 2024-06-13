package com.example.sel.base

import com.example.sel.base.model.HistoryExam
import com.example.sel.base.model.RequesPasword
import com.example.sel.base.model.RequestLogin
import com.example.sel.base.model.RequestRegister
import com.example.sel.base.model.RequestSubmitExams
import com.example.sel.base.model.RequestUpdatePass
import com.example.sel.base.model.ResponseLogin
import com.example.sel.base.model.ResponseMessage
import com.example.sel.base.model.ResponsePost
import com.example.sel.base.model.ResponseQuestionQuiz
import com.example.sel.base.model.ResponseRank
import com.example.sel.base.model.ResponseRegister
import com.example.sel.base.model.ResponseSubmitExams
import com.example.sel.base.model.ResponseTopic
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    companion object {
        private val gson: Gson = GsonBuilder().setDateFormat("dd/MM/yyyy").create()
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        private val okBuilder = OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS).retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor())

        val apiService: ApiService =
            Retrofit.Builder().baseUrl("https://b7a3-42-116-112-143.ngrok-free.app")
                .addConverterFactory(GsonConverterFactory.create(gson)).client(okBuilder.build())
                .build().create(ApiService::class.java)
    }



    @GET("/api/posts/{id}")
    suspend fun getAllQuestionHome(
        @Path("id") id: Int,
    ): Response<ResponseQuestionQuiz>

    @GET("/api/topics")
    suspend fun getAllTopic(): Response<ResponseTopic>
    @POST("/api/login")
    suspend fun getAPILogin(
        @Body input: RequestLogin,
    ): Response<ResponseLogin>

    @POST("/api/register")
    suspend fun getAPIRegister(
        @Body input: RequestRegister
    ): Response<ResponseRegister>

    @POST("api/update-password")
    suspend fun getAPIUpdatePass(
        @Body input: RequestUpdatePass
    ): Response<ResponseMessage>

    @PUT("/api/profile/update/{id}")
    suspend fun getAPIUpdateProfile(
        @Path("id") id: Int,
        @Body input: RequesPasword,
    ): Response<Any>

    @POST("/api/submit-exams/{id}")
    suspend fun getAPISubmitExams(
        @Path("id") id: Int,
        @Body input : RequestSubmitExams,
    ): Response<ResponseSubmitExams>

    @GET("/api/ranking?period=week")
    suspend fun getAPIRankMonth(): Response<List<ResponseRank>>

    @GET("/api/topics/{id}")
    suspend fun getAllPost(
        @Path("id") id: Int,
    ): Response<ResponsePost>

    @GET("/api/getHistoryByUser/{id}")
    suspend fun getHistoryByUser(@Path("id") id: Int): Response<List<HistoryExam>>

}