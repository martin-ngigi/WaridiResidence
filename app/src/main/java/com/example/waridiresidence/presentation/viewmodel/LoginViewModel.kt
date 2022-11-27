package com.example.waridiresidence.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelresponse.LoginResponse
import com.example.waridiresidence.domain.repository.LoginRepository
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
    private val repository: LoginRepository
//) : ViewModel(){  //ViewModel is also acceptable
) : AndroidViewModel(application){
    val TAG = "LoginPage"

    private val _loginData = MutableLiveData<Event<Resource<LoginResponse>>>()

    val  loginData: LiveData<Event<Resource<LoginResponse>>> = _loginData

    fun loginUser(loginRequest: LoginRequest) = viewModelScope.launch {
        getLogin(loginRequest)
    }

    suspend fun getLogin(loginRequest: LoginRequest) {
        _loginData.postValue(Event(Resource.Loading()))

        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getLogin(loginRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of login is successful.: ")
                    if ( !(response.body()!!.access.isNullOrEmpty())){ //not empty or not null
                        Log.i(TAG, "login is successful... ")
                        val successResponse: LoginResponse? = response.body()
                        toast(getApplication(), successResponse!!.message)
                        _loginData.postValue(Event(Resource.Success(response.body()!!)))

                        Log.i(TAG, "2. login is successful...\n RESPONSE IS:\n: Name: ${response.body()!!.user.firstName+' '+response.body()!!.user.lastName}")

                        saveUserDataInConstants(response.body()!!)
                    }
                    else if (response.body()!!.access.isEmpty()){
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
                    _loginData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception: ${e.message!!}")
                    Log.e(TAG, "Exception: ${e.message!!}", )
                }
            }
        }
        catch (t: Throwable){
            when (t){
                is IOException -> {
                    _loginData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                    Log.e(TAG, "Error: ${t.message!!}", )
                }
            }
        }
    }

    private fun saveUserDataInConstants(body: LoginResponse) {
        Constants.fname = body.user.firstName
        Constants.lname = body.user.lastLogin
        Constants.email = body.user.email
        Constants.phone = body.user.phone
        Constants.userType = body.user.userType
        Constants.access = body.access
        Constants.refresh = body.refresh

    }
}