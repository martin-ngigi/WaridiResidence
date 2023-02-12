package com.example.waridiresidence.presentation.ui.agent.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.waridiresidence.WaridiResidence
import com.example.waridiresidence.data.model.modelresponse.house.AllHousesResponse
import com.example.waridiresidence.data.model.modelresponse.house.HouseResponse
import com.example.waridiresidence.domain.repository.remote.firebase.WaridiRepositoryF
import com.example.waridiresidence.domain.repository.remote.retrofit.RetrofitRepository
import com.example.waridiresidence.util.Event
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.hasInternetConnection
import com.example.waridiresidence.util.toast


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AllHousesAgentViewModel @Inject constructor(
    application: Application,
    private val repository: RetrofitRepository,
    private val repositoryFirebase: WaridiRepositoryF,
) : AndroidViewModel(application){
    val TAG = "AllHousesViewModel"

    private val _houses = MutableLiveData<Event<Resource<AllHousesResponse>>>()
    val houses: LiveData<Event<Resource<AllHousesResponse>>> = _houses

    fun getHouses() = viewModelScope.launch {
        getAllHouses()
    }

    private suspend fun getAllHouses() {
        _houses.postValue(Event(Resource.Loading()))

        try {
            if (hasInternetConnection<WaridiResidence>()){
                val response = repository.getHouses()
                if (response.isSuccessful){
                    Log.i(TAG, "First stage of getting House is successful.")
                    if (response.body()!!.results.toString().isNotEmpty()){
                        val successResponse: AllHousesResponse?= response.body()
                        toast(getApplication(), "${successResponse!!.results.toString()}")
                        _houses.postValue(Event(Resource.Success(response.body()!!)))
                        Log.i(TAG, "getAllHouses: Getting all houses success. Response is ${response.body()!!.results}")
                    }
                    else if (response.body()!!.toString().isNullOrEmpty()){
                        val errorResponse: AllHousesResponse? = response.body()
                        toast(getApplication(), "Error encountered while getting houses. \n Hint $errorResponse")
                        Log.e(
                            TAG,
                            "getAllHouses: Error encountered while getting houses. \n Hint $errorResponse",
                        )
                    }
                }
            }
            else{
                _houses.postValue(Event(Resource.Error("No Internet Connection.\nPlease turn on cellular data or WiFi")))
                toast(getApplication(), "No Internet Connection.\nPlease turn on cellular data or WiFi")
                Log.e(TAG, "No Internet Connection.\nPlease turn on cellular data or WiFi", )

            }
        }
        catch (e: Exception){
            Log.e(TAG, "getAllHouses: Error: ${e.toString()}", )
            when(e){
                is IOException -> {
                    _houses.postValue(Event(Resource.Error(e.message?.toString())))
                    toast(getApplication(), "Exception: ${e.message?.toString()}")
                    Log.e(TAG, "getAllHouses: ${e.message?.toString()}", )
                }
            }
        }
        catch (t: Throwable){
            Log.e(TAG, "getAllHouses: Error: ${t.toString()}", )
            when(t){
                is IOException -> {
                    _houses.postValue(Event(Resource.Error(t.message?.toString())))
                    toast(getApplication(), "Exception: ${t.message?.toString()}")
                    Log.e(TAG, "getAllHouses: ${t.message?.toString()}", )
                }
            }
        }
    }

}