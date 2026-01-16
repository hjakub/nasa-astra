package com.example.ad_astra

import com.google.gson.annotations.SerializedName

data class ApodResponse(
    val title: String,
    val url: String,
    val explanation: String,

    @SerializedName("media_type")
    val mediaType: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?
)