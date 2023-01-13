package com.example.waridiresidence.util

class Constants {
    companion object{
        const val BASE_URL = "https://d39c-105-231-212-148.in.ngrok.io"
        var id: Int=0 //set it to zero.. then it will be updated later
        var fname = ""
        var lname = ""
        var email = ""
        var phone = ""
        var userType = ""
        var access = ""
        var refresh = ""
        var profile_image = ""
        var current_profile_image = ""
        var hasHouses = false
        var idUserHouse=0
        var currentHouseId: Int = 0
        var house_image_uri= ""

    }

    object FirebaseStorageConstants{
        val ROOT_DIRECTORY= "waridi"
        val  PROFILE_IMAGES = "profile"
        val  HOUSE_IMAGES = "house"
    }
}