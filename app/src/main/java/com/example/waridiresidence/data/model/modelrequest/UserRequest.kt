package com.example.waridiresidence.data.model.modelrequest


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class UserRequest(
    /**
     * {
    "email": "martinwainaina2@gmail.com",
    "username": "martinwainaina2@gmail.com",
    "phone": "0797292290",
    "first_name": "Martin",
    "last_name": "Wainaina",
    "password": "12345678",
    "gender": "F",
    "user_type": "C"
    }
     */
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("username")
    val username: String
): Serializable