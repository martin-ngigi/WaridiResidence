package com.example.waridiresidence.data.model.modelresponse.house


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
    {
        "id": 1,
        "title": "Kitchen Image",
        "description": "Best kitche ever",
        "url": "https://foyr.com/learn/wp-content/uploads/2021/06/one-wall-kitchen-design.jpg",
        "house": 1
    }
 */
@Keep
data class HouseImageResponse(
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