package com.example.waridiresidence.data.api

import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelresponse.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    /**
     *  API GITHUB LINK : https://github.com/martin-ngigi/Waridi-Homes-JWT-API
     */
    //loigin
    @POST("/auth/login2/")
    suspend fun getLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>
}