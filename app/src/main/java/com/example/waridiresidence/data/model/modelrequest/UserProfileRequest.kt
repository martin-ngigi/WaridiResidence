package com.example.waridiresidence.data.model.modelrequest


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class UserProfileRequest(
    /**
    {
    "phone": "07972922901",
    "first_name": "Martin1",
    "last_name": "Wainaina1",
    "profile_image": "https://avatars.githubusercontent.com/u/55280499?v=4"
    }
     */
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("profile_image")
    val profileImage: String
): Serializable