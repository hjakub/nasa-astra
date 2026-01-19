package com.example.ad_astra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class ApodViewModel : ViewModel() {

    private val _apod = MutableStateFlow<ApodResponse?>(null)
    val apod: StateFlow<ApodResponse?> = _apod

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val apiService: NasaApiService =
        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaApiService::class.java)

    fun loadApod(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getApod(
                    apiKey = apiKey,
                    date = _selectedDate.value
                )
                _apod.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setDateFromPicker(year: Int, month: Int, day: Int) {
        val cal = Calendar.getInstance().apply {
            set(year, month, day)
        }
        _selectedDate.value = dateFormat.format(cal.time)
    }

    fun getCalendar(): Calendar {
        val cal = Calendar.getInstance()
        _selectedDate.value?.let {
            cal.time = dateFormat.parse(it)!!
        }
        return cal
    }
}
