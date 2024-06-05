package com.example.automatedwhatsappmessage.api

import com.example.automatedwhatsappmessage.Common.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitClient {
    private var instance: ApiInterface? = null


    @Provides
    @Singleton
    fun getAPIInstance(): ApiInterface {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        //  val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
////            Log.d(TAG, "getConfigInstance: $BASE_URL")
        if (instance == null)
            instance = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(DomainInterceptor())
                        .addInterceptor(interceptor)
                        .build()
                )
                .baseUrl(BASE_URL)
                .build()
                .create(ApiInterface::class.java)
        return instance as ApiInterface
    }
}
