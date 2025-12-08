package com.example.ad_astra

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var titleText: TextView
    private lateinit var playButton: Button
    private lateinit var pickDateButton: Button

    private lateinit var apiService: NasaApiService
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    @SuppressLint("SetTextI18n")
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
        imageView = findViewById(R.id.imageView)
        titleText = findViewById(R.id.titleText)
        playButton = findViewById(R.id.playButton)
        pickDateButton = findViewById(R.id.pickDateButton)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(NasaApiService::class.java)

        // Load today's APOD initially
        loadApod(apiKey)

        // Date picker
        pickDateButton.setOnClickListener {
            showDatePicker(apiKey)
        }
    }

    private fun loadApod(apiKey: String, date: String? = null) {
        apiService.getApod(apiKey, date).enqueue(object : Callback<ApodResponse> {
            override fun onResponse(call: Call<ApodResponse>, response: Response<ApodResponse>) {
                if (response.isSuccessful) {
                    val apod = response.body()
                    if (apod != null) {
                        titleText.text = apod.title
                        Log.d("API_DEBUG", "APOD media_type=${apod.media_type}, url=${apod.url}")

                        var imageUrl: String
                        var videoUrl: String? = null

                        if (apod.media_type == "video") {
                            playButton.visibility = Button.VISIBLE

                            // Convert embed URL to watch URL
                            videoUrl = if (apod.url.contains("/embed/")) {
                                val videoId = apod.url.substringAfter("/embed/").substringBefore("?")
                                "https://www.youtube.com/watch?v=$videoId"
                            } else {
                                apod.url
                            }

                            imageUrl = if (videoUrl.contains("youtube.com/watch?v=")) {
                                val videoId = videoUrl.substringAfter("v=").substringBefore("&")
                                "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"
                            } else {
                                "https://via.placeholder.com/800x600.png?text=Video"
                            }

                            playButton.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                                startActivity(intent)
                            }

                        } else {
                            playButton.visibility = Button.GONE
                            imageUrl = apod.url
                        }

                        Glide.with(this@MainActivity)
                            .load(imageUrl)
                            .into(imageView)
                    }
                } else {
                    titleText.text = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ApodResponse>, t: Throwable) {
                titleText.text = "Failed: ${t.message}"
                Log.e("API_DEBUG", "Failed to fetch APOD", t)
            }
        })
    }

    private fun showDatePicker(apiKey: String) {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Set min date to 30 days ago
        calendar.add(Calendar.DAY_OF_MONTH, -30)
        val minDate = calendar.time.time
        val maxDate = today.time

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val pickedCalendar = Calendar.getInstance()
                pickedCalendar.set(year, month, dayOfMonth)
                val pickedDate = dateFormat.format(pickedCalendar.time)
                loadApod(apiKey, pickedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Restrict selectable range
        datePicker.datePicker.minDate = minDate
        datePicker.datePicker.maxDate = maxDate
        datePicker.show()
    }
}
