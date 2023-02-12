package com.example.waridiresidence.data.model.modelresponse.house


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AllHousesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Any,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<AllHousesResult>
)