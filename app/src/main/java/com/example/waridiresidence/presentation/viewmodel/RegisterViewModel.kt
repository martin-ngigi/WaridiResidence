package com.example.waridiresidence.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelrequest.UserRequest
import com.example.waridiresidence.data.model.modelresponse.UserResponse
import com.example.waridiresidence.domain.repository.remote.retrofit.RegisterRepository
import com.example.waridiresidence.util.Event
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.hasInternetConnection
import com.example.waridiresidence.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RegisterViewModel @Inject constructor(
    application: Application,
    private val repository: RegisterRepository
//) : ViewModel(){  //ViewModel is also acceptable
): AndroidViewModel(application){

    val TAG="RegisterPage"
    private val _registerData = MutableLiveData<Event<Resource<UserResponse>>>()
    val registerData : LiveData<Event<Resource<UserResponse>>> = _registerData

    fun registerUser(userRequest: UserRequest) = viewModelScope.launch {
        getRegister(userRequest)
    }

    private suspend fun getRegister(userRequest: UserRequest) {
        _registerData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getRegister(userRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of register is successful.: ")
                    if (!(response.body()!!.user.id.toString().isNullOrEmpty())){//if id is not null/empty means user was registered successfully
                        Log.i(TAG, "register is successful... ")
                        val successResponse: UserResponse? = response.body()
                        toast(getApplication(), successResponse!!.message)
                        _registerData.postValue(Event(Resource.Success(response.body()!!)))

                        Log.i(TAG, "2. register is successful...\n RESPONSE IS:\n:ID: ${response.body()!!.user.id} Name: ${response.body()!!.user.firstName+' '+response.body()!!.user.lastName}")
                    }
                }
                else if (response.body()!!.user.id.toString().isNullOrEmpty()){
                    val  errorResponse: UserResponse? = response.body()
                    toast(getApplication(), "Error: An error was encountered while login in ")
                    Log.e(TAG, "Error: An error was encountered while login in ", )
                }
            }
            else{
                _registerData.postValue(
                    Event(
                        Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "No Internet Connection.\nPlease turn on cellular data or WiFi", )

            }
        }
        catch (e: HttpException){
            when(e){
                is  IOException -> {
                    _registerData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception: ${e.message!!}")
                    Log.e(TAG, "Exception: ${e.message!!}" )
                }
            }
        }
        catch (t: Throwable){
            _registerData.postValue(Event(Resource.Error(t.message!!)))
            toast(getApplication(), "Exception: ${t.message!!}")
            Log.e(TAG, "Exception: ${t.message!!}" )
        }
    }
}