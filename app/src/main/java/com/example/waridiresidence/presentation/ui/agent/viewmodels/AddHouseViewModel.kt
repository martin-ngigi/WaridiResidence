package com.example.waridiresidence.presentation.ui.agent.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelrequest.house.HouseImageRequest
import com.example.waridiresidence.data.model.modelrequest.house.HouseRequest
import com.example.waridiresidence.data.model.modelresponse.house.HouseImageResponse
import com.example.waridiresidence.data.model.modelresponse.house.HouseResponse
import com.example.waridiresidence.domain.repository.remote.firebase.WaridiRepositoryF
import com.example.waridiresidence.domain.repository.remote.retrofit.RetrofitRepository
import com.example.waridiresidence.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AddHouseViewModel @Inject constructor(
    application: Application,
    private val repository: RetrofitRepository,
    private val repositoryFirebase: WaridiRepositoryF,
): AndroidViewModel(application){
    val TAG = "AddHouseViewModel"
    
    private val _addHouseData = MutableLiveData<Event<Resource<HouseResponse>>>()
    val addHouseData : LiveData<Event<Resource<HouseResponse>>> = _addHouseData

    private val _addHouseImages = MutableLiveData<Event<Resource<HouseImageResponse>>>()
    val addHouseImages : LiveData<Event<Resource<HouseImageResponse>>> = _addHouseImages
    
    fun addHouseDescription(houseRequest: HouseRequest) = viewModelScope.launch {
        getHouseDescription(houseRequest)
    }

    fun addHouseImages(houseImageRequest: HouseImageRequest) = viewModelScope.launch {
        getHouseImages(houseImageRequest)
    }

    private suspend fun getHouseImages(houseImageRequest: HouseImageRequest) {
        _addHouseImages.postValue(Event(Resource.Loading()))

        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getAddHouseImages(houseImageRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of Adding House Images is successful.: ")
                    if (response.body()!!.id.toString().isNotEmpty()){//if id not empty means that the data
                        val successResponse: HouseImageResponse?=response.body()
                        toast(getApplication(), "${successResponse!!.id.toString()}th House Images created successfully.")
                        _addHouseImages.postValue(Event(Resource.Success(response.body()!!)))
                        Log.i(TAG, "Adding house Images successful. Response: ${response.body()} ")

                    }
                    else if(response.body()!!.id.toString().isNotEmpty()){
                        val errorResponse: HouseImageResponse? = response.body()
                        toast(getApplication(), "Error encountered while adding house images.\n Hint $errorResponse")
                        Log.e(TAG, "getHouse: Error encountered while adding house images.\n Hint $errorResponse")
                    }

                }
            }
            else{
                _addHouseData.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "No Internet Connection.\nPlease turn on cellular data or WiFi", )
            }
        }
        catch (e: Exception){
            Log.e(TAG, "getHouse: Error: ${e.toString()}", )
            when(e){
                is IOException ->{
                    _addHouseData.postValue(Event(Resource.Error(e.message?.toString())))
                    toast(getApplication(), "Exception: ${e.message?.toString()}")
                    Log.e(TAG, "Exception: ${e.message?.toString()}", )
                }
            }
        }
        catch (t: Throwable){
            Log.e(TAG, "getHouse: Error: ${t.toString()}", )
            when(t){
                is IOException ->{
                    _addHouseData.postValue(Event(Resource.Error(t.message?.toString())))
                    toast(getApplication(), "Exception: ${t.message?.toString()}")
                    Log.e(TAG, "Exception: ${t.message?.toString()}", )
                }
            }
        }
    }

    private suspend fun getHouseDescription(houseRequest: HouseRequest) {
        _addHouseData.postValue(Event(Resource.Loading()))
        
        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getAddHouseDescription(houseRequest)
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of Adding House is successful.: ")
                    if (response.body()!!.id.toString().isNotEmpty()){//if id not empty means that the data 
                        Constants.currentHouseId = response.body()!!.id
                        val successResponse: HouseResponse?=response.body()
                        toast(getApplication(), "${successResponse!!.id.toString()}th House created successfully.")
                        _addHouseData.postValue(Event(Resource.Success(response.body()!!)))
                        Log.i(TAG, "Adding house successful. Response: ${response.body()} ")

                    }
                    else if(response.body()!!.id.toString().isNotEmpty()){
                        val errorResponse: HouseResponse? = response.body()
                        toast(getApplication(), "Error encountered while adding house.\n Hint $errorResponse")
                        Log.e(TAG, "getHouse: Error encountered while adding house.\n Hint $errorResponse")
                    }

                }
            }
            else{
                _addHouseData.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "No Internet Connection.\nPlease turn on cellular data or WiFi", )
            }
        }
        catch (e: Exception){
            Log.e(TAG, "getHouse: Error: ${e.toString()}", )
            when(e){
                is IOException ->{
                    _addHouseData.postValue(Event(Resource.Error(e.message?.toString())))
                    toast(getApplication(), "Exception: ${e.message?.toString()}")
                    Log.e(TAG, "Exception: ${e.message?.toString()}", )
                }
            }
        }
        catch (t: Throwable){
            Log.e(TAG, "getHouse: Error: ${t.toString()}", )
            when(t){
                is IOException ->{
                    _addHouseData.postValue(Event(Resource.Error(t.message?.toString())))
                    toast(getApplication(), "Exception: ${t.message?.toString()}")
                    Log.e(TAG, "Exception: ${t.message?.toString()}", )
                }
            }
        }
    }

    fun onUploadSingleFile(fileUris: Uri, onResult: (UiState<Uri>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repositoryFirebase.uploadSingleFile(fileUris, onResult)
        }
    }

}