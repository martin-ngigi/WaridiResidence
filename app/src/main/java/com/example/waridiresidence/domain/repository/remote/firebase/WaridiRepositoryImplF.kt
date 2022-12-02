package com.example.waridiresidence.domain.repository.remote.firebase

import android.net.Uri
import com.example.waridiresidence.util.Constants.FirebaseStorageConstants.PROFILE_IMAGES
import com.example.waridiresidence.util.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WaridiRepositoryImplF(
    val storageReference: StorageReference
): WaridiRepositoryF {
    override suspend fun uploadSingleFile(fileUri: Uri, onResult: (UiState<Uri>) -> Unit) {
        try {
            val uri: Uri= withContext(Dispatchers.IO){
                storageReference
                    .child(PROFILE_IMAGES)
                    .child("${System.currentTimeMillis()}")
                    .putFile(fileUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            onResult.invoke(UiState.Success(uri))
        }
        catch (e: FirebaseException){
            onResult.invoke(UiState.Failure(e.message))
        }
        catch (e: Exception){
            onResult.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun uploadMultipleFile(
        fileUri: List<Uri>,
        onResult: (UiState<List<Uri>>) -> Unit
    ) {
        try {
            val uri: List<Uri> = withContext(Dispatchers.IO) {
                fileUri.map { image ->
                    async {
                        storageReference.child(PROFILE_IMAGES).child(image.lastPathSegment ?: "${System.currentTimeMillis()}")
                            .putFile(image)
                            .await()
                            .storage
                            .downloadUrl
                            .await()
                    }
                }.awaitAll()
            }
            onResult.invoke(UiState.Success(uri))
        } catch (e: FirebaseException){
            onResult.invoke(UiState.Failure(e.message))
        }
        catch (e: FirebaseException){
            onResult.invoke(UiState.Failure(e.message))
        }
        catch (e: Exception){
            onResult.invoke(UiState.Failure(e.message))
        }
    }
}