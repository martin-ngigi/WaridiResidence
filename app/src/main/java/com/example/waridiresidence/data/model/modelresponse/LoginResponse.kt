package com.example.waridiresidence.data.model.modelresponse


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class LoginResponse(
    @SerializedName("access")
    val access: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("refresh")
    val refresh: String,
    @SerializedName("user")
    val user: UserLogin
): Serializable