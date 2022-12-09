package com.example.waridiresidence.domain.repository.remote.retrofit

import com.example.waridiresidence.data.api.ApiService
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
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getLogin(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.getLogin(loginRequest)
    }

    suspend fun getRegister(userRequest: UserRequest): Response<UserResponse>{
        return apiService.registerUser(userRequest)
    }

    suspend fun getUpdateUser(userRequest: UserProfileRequest): Response<UserProfileResponse>{
        return  apiService.updateUser(userRequest, Constants.id, "Bearer ${Constants.access}")
    }

    suspend fun getRegisterUserHouse(userHouseRequest: UserHouseRequest): Response<UserHouseResponse>{
        /**
         * Added Bearer to solve :
         * java.lang.NullPointerException
        at com.example.waridiresidence.presentation.viewmodel.LoginViewModel.getRegisterUserHouse(LoginViewModel.kt:154)
        at com.example.waridiresidence.presentation.viewmodel.LoginViewModel.access$getRegisterUserHouse(LoginViewModel.kt:20)
         */
        return apiService.registerUserHouse(userHouseRequest, "Bearer ${Constants.access}")
    }

}