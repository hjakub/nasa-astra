package com.example.ad_astra

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("planetary/apod")
    fun getApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String? = null
    ): Call<ApodResponse>
}