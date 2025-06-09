package com.avsoftware.domain.repository

import com.avsoftware.domain.model.Book
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getWantToReadList(): Single<List<Book>>
    fun getCurrentlyReading(): Single<List<Book>>
    fun getAlreadyRead(): Single<List<Book>>
}

interface FlowBookRepository {
    suspend fun getWantToReadList(): Flow<List<Book>>
    suspend fun getCurrentlyReading(): Flow<List<Book>>
    suspend fun getAlreadyRead(): Flow<List<Book>>
}