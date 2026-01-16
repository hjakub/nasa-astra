package com.example.ad_astra

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApodViewModel : ViewModel() {

    private val _apod = MutableStateFlow<ApodResponse?>(null)
    val apod: StateFlow<ApodResponse?> = _apod

    private val apiService: NasaApiService =
        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaApiService::class.java)

    fun loadApod(apiKey: String, date: String? = null) {
        viewModelScope.launch {
            try {
                val response = apiService.getApod(apiKey, date)


                Log.d("APOD_DEBUG", "MEDIA=${response.mediaType}")
                Log.d("APOD_DEBUG", "THUMB=${response.thumbnailUrl}")

                _apod.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
