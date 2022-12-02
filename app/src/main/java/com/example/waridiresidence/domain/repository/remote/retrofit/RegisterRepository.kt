package com.example.waridiresidence.domain.repository.remote.retrofit

import com.example.waridiresidence.data.api.ApiService
import com.example.waridiresidence.data.model.modelrequest.UserRequest
import com.example.waridiresidence.data.model.modelresponse.UserResponse
import retrofit2.Response
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val apiService: ApiService
) {



    suspend fun getRegister(userRequest: UserRequest): Response<UserResponse>{
        return apiService.registerUser(userRequest)
    }
}