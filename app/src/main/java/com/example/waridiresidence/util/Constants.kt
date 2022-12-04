package com.example.waridiresidence.util

class Constants {
    companion object{
        const val BASE_URL = "https://8f12-105-163-2-120.in.ngrok.io "
        var id: Int=0 //set it to zero.. then it will be updated later
        var fname = ""
        var lname = ""
        var email = ""
        var phone = ""
        var userType = ""
        var access = ""
        var refresh = ""
        var profile_image = ""
    }

    object FirebaseStorageConstants{
        val ROOT_DIRECTORY= "waridi"
        val  PROFILE_IMAGES = "profile"
    }
}