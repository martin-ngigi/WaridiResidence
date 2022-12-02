package com.example.waridiresidence.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waridiresidence.domain.repository.remote.firebase.WaridiRepositoryF
import com.example.waridiresidence.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: WaridiRepositoryF
): ViewModel() {

    fun onUploadSingleFile(fileUris: Uri, onResult: (UiState<Uri>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadSingleFile(fileUris, onResult)
        }
    }

    fun onUploadMultipleFile(fileUris: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadMultipleFile(fileUris, onResult)
        }
    }
}