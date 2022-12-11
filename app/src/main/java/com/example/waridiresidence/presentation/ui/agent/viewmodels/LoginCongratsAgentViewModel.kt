package com.example.waridiresidence.presentation.ui.agent.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelrequest.UserProfileRequest
import com.example.waridiresidence.data.model.modelrequest.house.UserHouseRequest
import com.example.waridiresidence.data.model.modelresponse.UserProfileResponse
import com.example.waridiresidence.data.model.modelresponse.house.UserHouseResponse
import com.example.waridiresidence.domain.repository.remote.firebase.WaridiRepositoryF
import com.example.waridiresidence.domain.repository.remote.retrofit.RetrofitRepository
import com.example.waridiresidence.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.HttpException
import java.io.IOException

@HiltViewModel
class LoginCongratsAgentViewModel @Inject constructor(
    application: Application,
    private val repositoryRetrofit: RetrofitRepository
): AndroidViewModel(application) {

    val TAG = "LoginCongrats2Page"

    private val _registerUserHouseData = MutableLiveData<Event<Resource<UserHouseResponse>>>()
    val registerUserHouseData: MutableLiveData<Event<Resource<UserHouseResponse>>> = _registerUserHouseData

    fun registerUserHouse(userHouseRequest: UserHouseRequest) = viewModelScope.launch {
        getUpdateUser(userHouseRequest)
    }

    private suspend fun getUpdateUser(userHouseRequest: UserHouseRequest) {
        _registerUserHouseData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repositoryRetrofit.getRegisterUserHouse(userHouseRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of updating is successful.: ")
                    if (response.body()!!.id.toString().isNotEmpty()){ //if id is not null/empty means user was registered successfully
                        Log.i(TAG, "updating is successful. ")
                        val successResponse: UserHouseResponse? = response.body()
                        toast(getApplication(), "${successResponse!!.agentName} Updated successfully")
                        _registerUserHouseData.postValue(Event(Resource.Success(response.body()!!)))
                        //save updated data to constants
                        Log.i(TAG, "2. Update is successful...\n RESPONSE IS:\n:ID: ${response.body()!!.id} Name: ${response.body()!!.agentName+' '+response.body()!!.id}")
                    }
                }
                else if (response.body()!!.id.toString().isNullOrEmpty()){
                    val errorResponse: UserHouseResponse? = response.body()
                    toast(getApplication(), "Error: An error was encountered while updating in.\nHint${errorResponse}")
                    Log.e(TAG, "Error: An error was encountered while updating in. \nHint${errorResponse} ", )
                }
            }
            else{
                _registerUserHouseData.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "getUpdateUser: \"No Internet Connection.\\nPlease turn on cellular data or WiFi\"", )
            }
        }
        catch (e: HttpException){
            when(e){
                is IOException ->{
                    _registerUserHouseData.postValue(Event((Resource.Error(e.message!!))))
                    toast(getApplication(), "Exception: ${e.message!!}")
                    Log.e(TAG, "Exception: ${e.message!!}" )                }
            }
        }
        catch (e: Exception){
            _registerUserHouseData.postValue(Event(Resource.Error(e.message!!)))
            toast(getApplication(), "Exception: ${e.message!!}")
            Log.e(TAG, "Exception: ${e.toString()}" )
        }
        catch (t: Throwable){
            _registerUserHouseData.postValue(Event(Resource.Error(t.message!!)))
            toast(getApplication(), "Exception: ${t.message!!}")
            Log.e(TAG, "Exception: ${t.toString()}" )
        }
    }

    private fun updatedConstantsUserData(successResponse: UserProfileResponse) {
        Constants.fname = successResponse!!.firstName
        Constants.lname = successResponse!!.lastName
        Constants.phone = successResponse!!.phone
        Constants.profile_image = successResponse!!.profileImage
    }

}