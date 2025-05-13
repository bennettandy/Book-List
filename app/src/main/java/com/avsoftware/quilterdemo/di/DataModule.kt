package com.avsoftware.quilterdemo.di

import com.avsoftware.quilterdemo.data.api.OpenLibraryApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    companion object {
        private const val BASE_URL = "https://openlibrary.org/"
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideOpenLibraryApiService(retrofit: Retrofit): OpenLibraryApiService = retrofit
                .create(OpenLibraryApiService::class.java)

}