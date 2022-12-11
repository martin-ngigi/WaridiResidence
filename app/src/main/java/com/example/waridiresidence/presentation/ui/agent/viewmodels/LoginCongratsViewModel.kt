package com.example.waridiresidence.presentation.ui.agent.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelrequest.house.UserHouseRequest
import com.example.waridiresidence.data.model.modelresponse.LoginResponse
import com.example.waridiresidence.data.model.modelresponse.UserResponse
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
class LoginCongratsViewModel @Inject constructor(
    application: Application,
    private val repository: RetrofitRepository
//) : ViewModel(){  //ViewModel is also acceptable
) : AndroidViewModel(application){
    val TAG = "Congrats22ViewModel"

    private val _userHouse22Data = MutableLiveData<Event<Resource<UserHouseResponse>>>()
    val  userHouse22Data: LiveData<Event<Resource<UserHouseResponse>>> = _userHouse22Data


    fun registerUserHouse22(userHouseRequest: UserHouseRequest) = viewModelScope.launch {
        getLogin(userHouseRequest)
    }

    private suspend fun getLogin(userHouseRequest: UserHouseRequest) {
        _userHouse22Data.postValue(Event(Resource.Loading()))

        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getRegisterUserHouse22(userHouseRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of login is successful.: ")
                    if ( response.body()!!.id.toString().isNotEmpty()){ //not empty or not null
                        Log.i(TAG, "login is successful... ")
                        val successResponse: UserHouseResponse? = response.body()
                        toast(getApplication(), successResponse!!.agentName +" Created successfully")
                        _userHouse22Data.postValue(Event(Resource.Success(response.body()!!)))

                        Log.i(TAG, "2. login is successful...\n RESPONSE IS:\n: Name: ${response.body()!!.agentName+' '+response.body()!!.id}")

                    }
                    else if (response.body()!!.id.toString().isNullOrEmpty()){
                        val errorResponse: UserHouseResponse? = response.body()
                        toast(getApplication(), "Error: An error was encountered while login in ")
                        Log.e(TAG, "Error: An error was encountered while login in ", )
                    }
                }
                else {
                    _userHouse22Data.postValue(Event(Resource.Error(response.message())))
                    Log.e(TAG, "Error: "+response.message(), )
                }
            }

            else{
                _userHouse22Data.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "No Internet Connection.\nPlease turn on cellular data or WiFi", )

            }
        }
        catch (e: HttpException){
            when(e){
                is IOException -> {
                    _userHouse22Data.postValue(Event(Resource.Error(e.message?.toString())))
                    toast(getApplication(), "Exception: ${e.message?.toString()}")
                    Log.e(TAG, "Exception: ${e.message?.toString()}", )
                }
            }
        }
        catch (t: Throwable){
            when (t){
                is IOException -> {
                    _userHouse22Data.postValue(Event(Resource.Error(t.message?.toString())))
                    toast(getApplication(), "Exception: ${t.message?.toString()}")
                    Log.e(TAG, "Exception: ${t.message?.toString()}", )
                }
            }
        }
    }



}