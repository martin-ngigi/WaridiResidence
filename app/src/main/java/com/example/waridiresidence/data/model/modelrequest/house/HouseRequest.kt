package com.example.waridiresidence.data.model.modelrequest.house


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class HouseRequest(
    /**
     * {
    "user":2,
    "title": "House 2",
    "category": "two Bedroom",
    "description": " Very spacious 2 room",
    "location": "Lavington, Nairobi",
    "status": "Vacant",
    "monthly_price": 200000
    }
     */
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("monthly_price")
    val monthlyPrice: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("user")
    val user: Int
)