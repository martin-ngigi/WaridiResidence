package com.example.waridiresidence.domain.repository.remote.retrofit

import com.example.waridiresidence.data.api.ApiService
import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelresponse.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun getLogin(loginRequest: LoginRequest): Response<LoginResponse>{
        return apiService.getLogin(loginRequest)
    }
}