package com.example.waridiresidence.presentation.ui.client.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelrequest.UserProfileRequest
import com.example.waridiresidence.data.model.modelresponse.UserProfileResponse
import com.example.waridiresidence.domain.repository.remote.firebase.WaridiRepositoryF
import com.example.waridiresidence.domain.repository.remote.retrofit.RetrofitRepository
import com.example.waridiresidence.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.HttpException
import java.io.IOException

@HiltViewModel
class ProfileClientViewModel @Inject constructor(
    application: Application,
    private val repositoryRetrofit: RetrofitRepository,
    private val repositoryFirebase: WaridiRepositoryF,
): AndroidViewModel(application) {

    val TAG = "ProfilePage"

    private val _updateUserData = MutableLiveData<Event<Resource<UserProfileResponse>>>()
    val updateUserData: MutableLiveData<Event<Resource<UserProfileResponse>>> = _updateUserData

    fun updateUser(userProfileRequest: UserProfileRequest) = viewModelScope.launch {
        getUpdateUser(userProfileRequest)
    }

    private suspend fun getUpdateUser(userRequest: UserProfileRequest) {
        _updateUserData.postValue(Event((Resource.Loading())))
        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repositoryRetrofit.getUpdateUser(userRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of updating is successful.: ")
                    if (response.body()!!.id.toString().isNotEmpty()){ //if id is not null/empty means user was registered successfully
                        Log.i(TAG, "updating is successful. ")
                        val successResponse: UserProfileResponse? = response.body()
                        toast(getApplication(), "${successResponse!!.firstName} Updated successfully")
                        _updateUserData.postValue(Event(Resource.Success(response.body()!!)))
                        //save updated data to constants
                        updatedConstantsUserData(successResponse)
                        Log.i(TAG, "2. Update is successful...\n RESPONSE IS:\n:ID: ${response.body()!!.id} Name: ${response.body()!!.firstName+' '+response.body()!!.lastName}")
                    }
                }
                else if (response.body()!!.id.toString().isNullOrEmpty()){
                    val errorResponse: UserProfileResponse? = response.body()
                    toast(getApplication(), "Error: An error was encountered while updating in.\nHint${errorResponse}")
                    Log.e(TAG, "Error: An error was encountered while updating in. \nHint${errorResponse} ", )
                }
            }
            else{
                _updateUserData.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "getUpdateUser: \"No Internet Connection.\\nPlease turn on cellular data or WiFi\"", )
            }
        }
        catch (e: HttpException){
            when(e){
                is IOException ->{
                    _updateUserData.postValue(Event((Resource.Error(e.message!!))))
                    toast(getApplication(), "Exception: ${e.message!!}")
                    Log.e(TAG, "Exception: ${e.message!!}" )                }
            }
        }
        catch (e: Exception){
            _updateUserData.postValue(Event(Resource.Error(e.message!!)))
            toast(getApplication(), "Exception: ${e.message!!}")
            Log.e(TAG, "Exception: ${e.toString()}" )
        }
        catch (t: Throwable){
            _updateUserData.postValue(Event(Resource.Error(t.message!!)))
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

    fun onUploadSingleFile(fileUris: Uri, onResult: (UiState<Uri>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repositoryFirebase.uploadSingleFile(fileUris, onResult)
        }
    }

    fun onUploadMultipleFile(fileUris: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repositoryFirebase.uploadMultipleFile(fileUris, onResult)
        }
    }
}