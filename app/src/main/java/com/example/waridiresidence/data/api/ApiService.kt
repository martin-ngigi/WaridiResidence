package com.example.waridiresidence.data.api

import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelrequest.UserClientProfileRequest
import com.example.waridiresidence.data.model.modelrequest.UserAgentProfileRequest
import com.example.waridiresidence.data.model.modelrequest.UserRequest
import com.example.waridiresidence.data.model.modelrequest.house.HouseImageRequest
import com.example.waridiresidence.data.model.modelrequest.house.HouseRequest
import com.example.waridiresidence.data.model.modelrequest.house.UserHouseRequest
import com.example.waridiresidence.data.model.modelresponse.LoginResponse
import com.example.waridiresidence.data.model.modelresponse.UserClientProfileResponse
import com.example.waridiresidence.data.model.modelresponse.UserAgentProfileResponse
import com.example.waridiresidence.data.model.modelresponse.UserResponse
import com.example.waridiresidence.data.model.modelresponse.house.AllHousesResponse
import com.example.waridiresidence.data.model.modelresponse.house.HouseImageResponse
import com.example.waridiresidence.data.model.modelresponse.house.HouseResponse
import com.example.waridiresidence.data.model.modelresponse.house.UserHouseResponse
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
    suspend fun registerUserHouse22(@Body userHouseRequest: UserHouseRequest, @Header("Authorization") access: String): Response<UserHouseResponse>


    //update user profile
    @Headers("Content-Type: application/json")
    @PUT("/auth/update/{pk}/")
    suspend fun  updateUser(@Body userAgentProfileRequest: UserAgentProfileRequest, @Path("pk") id:Int, @Header("Authorization")access: String): Response<UserAgentProfileResponse>


    //update user profile
    @Headers("Content-Type: application/json")
    @PUT("/auth/update/{pk}/")
    suspend fun  updateClientUser(@Body userClientProfileRequest: UserClientProfileRequest,@Path("pk") id:Int, @Header("Authorization")access: String): Response<UserClientProfileResponse>

    @POST("/houses/houses/")
    suspend fun addHouseDescription(@Body houseRequest: HouseRequest, @Header("Authorization") access: String): Response<HouseResponse>

    @Headers("Content-Type: application/json")
    @POST("/houses/images/")
    suspend fun  addHouseImages(@Body houseImageRequest: HouseImageRequest, @Header("Authorization") access: String): Response<HouseImageResponse>

    @GET("/houses/houses/")
    suspend fun getHouses(@Header("Authorization") access: String): Response<AllHousesResponse>

}