package com.example.waridiresidence.data.model.modelresponse.house


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AllImagesResponse(
    @SerializedName("description")
    val description: String,
    @SerializedName("house")
    val house: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)