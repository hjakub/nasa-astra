package com.example.ad_astra

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiKey = BuildConfig.NASA_API_KEY
        Log.d("API_DEBUG", "Using API key: '$apiKey'")

        val imageView = findViewById<ImageView>(R.id.imageView)
        val titleText = findViewById<TextView>(R.id.titleText)

        // retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NasaApiService::class.java)

        // fetch apod data
        apiService.getApod(apiKey).enqueue(object : Callback<ApodResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ApodResponse>, response: Response<ApodResponse>) {
                if (response.isSuccessful) {
                    val apod = response.body()
                    if (apod != null) {
                        titleText.text = apod.title
                        Glide.with(this@MainActivity)
                            .load(apod.url)
                            .into(imageView)
                    }
                } else {
                    titleText.text = "Error: ${response.code()}"
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<ApodResponse>, t: Throwable) {
                titleText.text = "Failed: ${t.message}"
            }
        })
    }
}
