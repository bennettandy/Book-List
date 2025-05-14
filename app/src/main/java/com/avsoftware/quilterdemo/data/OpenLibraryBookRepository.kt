package com.avsoftware.quilterdemo.data

import com.avsoftware.quilterdemo.data.api.OpenLibraryApiService
import com.avsoftware.quilterdemo.data.api.model.ReadingLogResponse
import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.model.BookStatus
import com.avsoftware.quilterdemo.domain.repository.BookRepository
import io.reactivex.Single
import javax.inject.Inject

class OpenLibraryBookRepository @Inject constructor(
    private val openLibraryApiService: OpenLibraryApiService
): BookRepository {

    override fun getWantToReadList(): Single<List<Book>> {
        return openLibraryApiService.getWantToRead().map {
            response: ReadingLogResponse -> response.reading_log_entries.map { Book(id = it.work.key, it.work.title, it.work.author_names.firstOrNull().orEmpty(),
            BookStatus.WantToRead) }
        }
    }

    override fun getCurrentlyReading(): Single<List<Book>> {
        return openLibraryApiService.getCurrentlyReading().map {
                response: ReadingLogResponse -> response.reading_log_entries.map { Book(id = it.work.key, it.work.title, it.work.author_names.firstOrNull().orEmpty(),
            BookStatus.CurrentlyReading) }
        }
    }

    override fun getAlreadyRead(): Single<List<Book>> {
        return openLibraryApiService.getAlreadyRead().map {
                response: ReadingLogResponse -> response.reading_log_entries.map { Book(id = it.work.key, it.work.title, it.work.author_names.firstOrNull().orEmpty(),
            BookStatus.AlreadyRead) }
        }
    }



}