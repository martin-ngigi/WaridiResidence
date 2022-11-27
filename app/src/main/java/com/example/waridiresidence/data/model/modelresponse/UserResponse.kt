package com.example.waridiresidence.data.model.modelresponse


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class UserResponse(
    /**
     *
     {
        "message": "User Created Successfully",
        "user": {
            "id": 6,
            "email": "martinwainaina4@gmail.com",
            "username": "martinwainaina4@gmail.com",
            "last_login": "2022-11-23T07:18:46.300946Z",
            "is_superuser": false,
            "first_name": "Martin",
            "last_name": "Wainaina",
            "is_staff": false,
            "is_active": true,
            "date_joined": "2022-11-23T07:18:45.681629Z",
            "phone": "0797292290",
            "gender": "F",
            "user_type": "C",
            "groups": [],
            "user_permissions": []
        }
    }
     */
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User
): Serializable