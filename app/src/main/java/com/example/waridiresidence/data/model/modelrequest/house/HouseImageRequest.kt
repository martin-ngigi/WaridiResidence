package com.example.waridiresidence.data.model.modelrequest.house


import android.net.Uri
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import retrofit2.http.Url

/**
    {
        "house":1,
        "title": "Kitchen Image",
        "description": "Best kitche ever",
        "url": "https://foyr.com/learn/wp-content/uploads/2021/06/one-wall-kitchen-design.jpg"
    }
 */

@Keep
data class HouseImageRequest(
    @SerializedName("description")
    val description: String,
    @SerializedName("house")
    val house: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)