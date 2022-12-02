package com.example.waridiresidence.presentation.di.firebase

import com.example.waridiresidence.domain.repository.remote.firebase.WaridiRepositoryF
import com.example.waridiresidence.domain.repository.remote.firebase.WaridiRepositoryImplF
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWaridiRepository(
        storageReference: StorageReference
    ):  WaridiRepositoryF{
        return WaridiRepositoryImplF(storageReference)
    }
}