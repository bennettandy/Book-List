package com.avsoftware.quilterdemo.domain.repository

import com.avsoftware.quilterdemo.domain.model.Book
import io.reactivex.Single

interface BookRepository {
    fun getWantToReadList(): Single<List<Book>>
    fun getCurrentlyReading(): Single<List<Book>>
    fun getAlreadyRead(): Single<List<Book>>
}