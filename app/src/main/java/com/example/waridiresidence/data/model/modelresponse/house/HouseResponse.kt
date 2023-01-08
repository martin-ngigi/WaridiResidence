package com.example.waridiresidence.data.model.modelresponse.house


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class HouseResponse(
    @SerializedName("add_date")
    val addDate: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: List<Image>,
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