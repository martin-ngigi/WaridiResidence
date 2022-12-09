package com.example.waridiresidence.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelrequest.house.UserHouseRequest
import com.example.waridiresidence.data.model.modelresponse.LoginResponse
import com.example.waridiresidence.data.model.modelresponse.house.UserHouseResponse
import com.example.waridiresidence.domain.repository.remote.retrofit.RetrofitRepository
import com.example.waridiresidence.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: RetrofitRepository
//) : ViewModel(){  //ViewModel is also acceptable
) : AndroidViewModel(application){
    val TAG = "LoginPage"

    private val _loginData = MutableLiveData<Event<Resource<LoginResponse>>>()
    val  loginData: LiveData<Event<Resource<LoginResponse>>> = _loginData

    private val _registerUserHouseData = MutableLiveData<Event<Resource<UserHouseResponse>>>()
    val registerUserHouseData: LiveData<Event<Resource<UserHouseResponse>>> = _registerUserHouseData


    fun loginUser(loginRequest: LoginRequest) = viewModelScope.launch {
        getLogin(loginRequest)
    }

    fun registerUserHouse(userHouseRequest: UserHouseRequest) =viewModelScope.launch {
        getRegisterUserHouse(userHouseRequest)
    }

    private suspend fun getLogin(loginRequest: LoginRequest) {
        _loginData.postValue(Event(Resource.Loading()))

        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getLogin(loginRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of login is successful.: ")
                    if ( response.body()!!.access.isNotEmpty()){ //not empty or not null
                        Log.i(TAG, "login is successful... ")
                        val successResponse: LoginResponse? = response.body()
                        toast(getApplication(), successResponse!!.message)
                        _loginData.postValue(Event(Resource.Success(response.body()!!)))

                        Log.i(TAG, "2. login is successful...\n RESPONSE IS:\n: Name: ${response.body()!!.user.firstName+' '+response.body()!!.user.lastName}")

                        saveUserDataInConstants(response.body()!!)
                    }
                    else if (response.body()!!.access.isNullOrEmpty()){
                        val errorResponse: LoginResponse? = response.body()
                        toast(getApplication(), "Error: An error was encountered while login in ")
                        Log.e(TAG, "Error: An error was encountered while login in ", )
                    }
                }
                else {
                    _loginData.postValue(Event(Resource.Error(response.message())))
                    Log.e(TAG, "Error: "+response.message(), )
                }
            }

            else{
                _loginData.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "No Internet Connection.\nPlease turn on cellular data or WiFi", )

            }
        }
        catch (e: HttpException){
            when(e){
                is IOException -> {
                    _loginData.postValue(Event(Resource.Error(e.message?.toString())))
                    toast(getApplication(), "Exception: ${e.message?.toString()}")
                    Log.e(TAG, "Exception: ${e.message?.toString()}", )
                }
            }
        }
        catch (t: Throwable){
            when (t){
                is IOException -> {
                    _loginData.postValue(Event(Resource.Error(t.message?.toString())))
                    toast(getApplication(), "Exception: ${t.message?.toString()}")
                    Log.e(TAG, "Exception: ${t.message?.toString()}", )
                }
            }
        }
    }

    private fun saveUserDataInConstants(body: LoginResponse) {
        Constants.id = body.user.id
        Constants.fname = body.user.firstName
        Constants.lname = body.user.lastName
        Constants.email = body.user.email
        Constants.phone = body.user.phone
        Constants.userType = body.user.userType
        Constants.access = body.access
        Constants.refresh = body.refresh
        Constants.profile_image = body.user.profileImage
        Constants.userType = body.user.userType
        Constants.hasHouses = body.user.hasHouses


    }

    private suspend fun getRegisterUserHouse(userHouseRequest: UserHouseRequest) {
        _registerUserHouseData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getRegisterUserHouse(userHouseRequest)
                Log.i(TAG, "First stage of getRegisterUserHouse Success: ")
                if (response.isSuccessful){
                    if (response.body()!!.id.toString().isNotEmpty()){ //if id is not empty means that the user-house has been created successfuly
                        Log.i(TAG, "getRegisterUserHouse is Successful: ")
                        val successResponse : UserHouseResponse? = response.body()
                        toast(getApplication(), "House description added successfully")
                        _registerUserHouseData.postValue(Event(Resource.Success(response.body()!!)))
                    }
                }
                else if (response.body()!!.id.toString().isEmpty()){
                    val errorResponse: UserHouseResponse?= response.body()
                    toast(getApplication(), "Error occurred while posting data.\nHint ${errorResponse}")
                    Log.e(TAG, "getRegisterUserHouse Error: \nError occurred while posting data.\nHint ${errorResponse}" )
                }
            }
            else{
                _registerUserHouseData.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "No Internet Connection.\nPlease turn on cellular data or WiFi", )

            }
        }
        catch (e: HttpException){
            when(e){
                is IOException -> {
                    _registerUserHouseData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception: ${e.message!!}")
                    Log.e(TAG, "Exception: ${e.message!!}", )
                }
            }
        }
        catch (t: Throwable){
            _registerUserHouseData.postValue(Event(Resource.Error(t.message!!)))
            toast(getApplication(), "Exception: ${t.message!!}")
            Log.e(TAG, "Exception: ${t.message!!}", )
        }
    }

}