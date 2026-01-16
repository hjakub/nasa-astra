package com.example.ad_astra

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import java.text.SimpleDateFormat
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import java.util.*

fun getApodImageUrl(apod: ApodResponse): String {
    return when (apod.mediaType) {
        "image" -> apod.url

        "video" -> apod.thumbnailUrl
            ?: "https://via.placeholder.com/800x600.png?text=Video"

        else -> "https://via.placeholder.com/800x600.png?text=NASA+Video"
    }
}

@Composable
fun ApodScreen(
    apiKey: String,
    viewModel: ApodViewModel = viewModel()
) {
    val apod by viewModel.apod.collectAsState()
    val context = LocalContext.current

    val dateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }

    var selectedDate by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadApod(apiKey)
    }

    val calendar = remember { Calendar.getInstance() }

    fun showDatePicker() {
        val today = Calendar.getInstance()

        val minDateCal = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -30)
        }

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val picked = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                selectedDate = dateFormat.format(picked.time)
                viewModel.loadApod(apiKey, selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minDateCal.timeInMillis
            datePicker.maxDate = today.timeInMillis
        }.show()
    }

    apod?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = it.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            val imageUrl = remember(it) {
                getApodImageUrl(it)
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = it.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            if (it.mediaType != "image") {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(it.url)
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Play video")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showDatePicker() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Pick date")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = it.explanation)
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
