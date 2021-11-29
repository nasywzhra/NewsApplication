package com.nasywa.newsapplication.Service

import com.nasywa.newsapplication.model.ResponseNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    fun getNewsHeadLines(
        @Query("country") country: String?,
        @Query("apiKey") apiKey:String?

    ): Call<ResponseNews>
}