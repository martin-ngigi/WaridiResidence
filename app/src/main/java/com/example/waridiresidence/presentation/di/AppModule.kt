package com.example.waridiresidence.presentation.di

import com.example.waridiresidence.data.api.ApiService
import com.example.waridiresidence.domain.repository.remote.retrofit.RetrofitRepository
import com.example.waridiresidence.util.Constants
import com.example.waridiresidence.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesBaseUrl(): String{
       return BASE_URL
    }

    @Provides
    fun providesHttpLoginInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun providesOkhttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        return okHttpClient.build()
    }

    @Provides
    fun providesConverterFactory(): Converter.Factory{
        return GsonConverterFactory.create()
    }

    //doesn't have access token/authorizatio/bearer since access token are only provided at login
    @Provides
    fun providesRetrofitAtRegisterLevel(
        baseUrl: String,
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
    }


    //url - > https://stackoverflow.com/questions/41078866/retrofit2-authorization-global-interceptor-for-access-token

//    //for login and the rest of the levels after loggin since access tokens are needed....
//    @Provides
//    fun providesRetrofit(
//        baseUrl: String,
//        converterFactory: Converter.Factory,
//        client: OkHttpClient
//    ): Retrofit{
//        return Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(converterFactory)
//            //.client(client)
//            .client(OkHttpClient.Builder().addInterceptor { chain ->
//                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${Constants.access}").build()
//                chain.proceed(request)
//            }.build())
//            .build()
//    }

    @Provides
    fun providesRetrofitService(retrofit: Retrofit): ApiService{
        return retrofit.create(ApiService::class.java)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun provideRetrofitRepository( apiService: ApiService): RetrofitRepository {
        return RetrofitRepository(apiService)
    }


}