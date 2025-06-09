package com.avsoftware.data.api

import com.avsoftware.data.api.model.ReadingLogResponse
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface OpenLibraryApiService {

    @GET("people/mekBot/books/want-to-read.json")
    fun getWantToRead(): Single<ReadingLogResponse>

    @GET("people/mekBot/books/currently-reading.json")
    fun getCurrentlyReading(): Single<ReadingLogResponse>

    @GET("people/mekBot/books/already-read.json")
    fun getAlreadyRead(): Single<ReadingLogResponse>

}

interface OpenLibrarySimpleApiService {

    @GET("people/mekBot/books/want-to-read.json")
    suspend fun getWantToRead(): Response<ReadingLogResponse>

    @GET("people/mekBot/books/currently-reading.json")
    suspend fun getCurrentlyReading(): Response<ReadingLogResponse>

    @GET("people/mekBot/books/already-read.json")
    suspend fun getAlreadyRead(): Response<ReadingLogResponse>

}