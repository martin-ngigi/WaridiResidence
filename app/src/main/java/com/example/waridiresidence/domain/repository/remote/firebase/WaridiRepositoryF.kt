package com.example.waridiresidence.domain.repository.remote.firebase

import android.net.Uri
import com.example.waridiresidence.util.UiState


interface WaridiRepositoryF {
    suspend fun uploadSingleProfileFile(fileUri: Uri, onResult: (UiState<Uri>) -> Unit)
    suspend fun uploadMultipleFile(fileUri: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit)
    suspend fun uploadSingleHouseFile(fileUri: Uri, onResult: (UiState<Uri>) -> Unit)
}