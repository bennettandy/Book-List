package com.avsoftware.data

import com.avsoftware.data.api.OpenLibraryApiService
import com.avsoftware.data.api.model.ReadingLogEntry
import com.avsoftware.domain.model.Book
import com.avsoftware.domain.model.BookStatus
import com.avsoftware.domain.repository.BookRepository
import io.reactivex.Single
import javax.inject.Inject

class OpenLibraryBookRepository @Inject constructor(
    private val openLibraryApiService: OpenLibraryApiService
): BookRepository {

    override fun getWantToReadList(): Single<List<Book>> {
        return openLibraryApiService.getWantToRead().map{
            it.reading_log_entries.map { it.toBook(BookStatus.WantToRead) }
        }
    }

    override fun getCurrentlyReading(): Single<List<Book>> {
        return openLibraryApiService.getCurrentlyReading().map {
            it.reading_log_entries.map { it.toBook(BookStatus.CurrentlyReading) }

        }
    }

    override fun getAlreadyRead(): Single<List<Book>> {
        return openLibraryApiService.getAlreadyRead().map {
            it.reading_log_entries.map { it.toBook(BookStatus.AlreadyRead) }

        }
    }

    private fun ReadingLogEntry.toBook(bookStatus: BookStatus) = Book(
        id = work.key,
        title = work.title,
        author = work.author_names.firstOrNull().orEmpty(),
        coverId = work.cover_id,
        status = bookStatus
    )
}