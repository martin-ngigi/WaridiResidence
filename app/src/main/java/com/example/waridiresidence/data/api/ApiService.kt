package com.example.waridiresidence.data.api

import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelrequest.UserProfileRequest
import com.example.waridiresidence.data.model.modelrequest.UserRequest
import com.example.waridiresidence.data.model.modelrequest.house.UserHouseRequest
import com.example.waridiresidence.data.model.modelresponse.LoginResponse
import com.example.waridiresidence.data.model.modelresponse.UserProfileResponse
import com.example.waridiresidence.data.model.modelresponse.UserResponse
import com.example.waridiresidence.data.model.modelresponse.house.UserHouseResponse
import com.example.waridiresidence.util.Constants
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    /**
     *  API GITHUB LINK : https://github.com/martin-ngigi/Waridi-Homes-JWT-API
     */
    //loigin
    @POST("/auth/login2/")
    suspend fun getLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    //register
    @POST("/auth/signup/")
    suspend fun registerUser(@Body userRequest: UserRequest): Response<UserResponse>

    //register user record for storing houses, images... i.e separate users accounts from houses accounts
    @POST("/houses/users/")
    suspend fun registerUserHouse(@Body userHouseRequest: UserHouseRequest, @Header("Authorization") access: String): Response<UserHouseResponse>

    //update user profile
    @Headers("Content-Type: application/json")
    @PUT("/auth/update/{pk}/")
//    suspend fun  updateUser(@Body userProfileRequest: UserProfileRequest,@Path(value = "pk")id:Int): Response<UserProfileResponse>
    //suspend fun  updateUser(@Body userProfileRequest: UserProfileRequest,@Path("pk") id:Int, @Header("Authorization") value:String ): Response<UserProfileResponse>
    suspend fun  updateUser(@Body userProfileRequest: UserProfileRequest,@Path("pk") id:Int, @Header("Authorization")access: String): Response<UserProfileResponse>
}