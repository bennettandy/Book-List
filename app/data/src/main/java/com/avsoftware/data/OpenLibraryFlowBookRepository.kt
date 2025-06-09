package com.avsoftware.data

import com.avsoftware.data.api.OpenLibrarySimpleApiService
import com.avsoftware.data.api.model.ReadingLogEntry
import com.avsoftware.data.api.model.ReadingLogResponse
import com.avsoftware.domain.model.Book
import com.avsoftware.domain.model.BookStatus
import com.avsoftware.domain.repository.FlowBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class OpenLibraryFlowBookRepository @Inject constructor(
    private val openLibraryFlowApiService: OpenLibrarySimpleApiService
): FlowBookRepository {

    override suspend fun getWantToReadList(): Flow<List<Book>> = processResponse(
        endpoint = openLibraryFlowApiService::getWantToRead,
        status = BookStatus.WantToRead
    )

    private fun processResponse(endpoint: suspend () -> Response<ReadingLogResponse>, status: BookStatus): Flow<List<Book>> {
        return flow {
            val response = endpoint()

            if (response.isSuccessful) {

                val success: List<Book> = response.body()?.let { readingLogResponse: ReadingLogResponse ->
                    readingLogResponse.reading_log_entries.map { it.toBook(status) }
                } ?: emptyList()
                emit(success)

            } else {
                throw BookFetchException("Empty response body for $status")
            }
        }
    }

    override suspend fun getCurrentlyReading(): Flow<List<Book>> = processResponse(
        endpoint = openLibraryFlowApiService::getCurrentlyReading,
        status = BookStatus.CurrentlyReading
    )

    override suspend fun getAlreadyRead(): Flow<List<Book>> = processResponse(
        endpoint = openLibraryFlowApiService::getAlreadyRead,
        status = BookStatus.AlreadyRead
    )

    private fun fetchBooks(response: Response<ReadingLogResponse>, status: BookStatus): List<Book> {
        if (response.isSuccessful) {
            val readingLog = response.body()
                ?: throw BookFetchException("Empty response body for $status")
            return readingLog.reading_log_entries.map { it.toBook(status) }
        } else {
            throw BookFetchException("API error: ${response.code()} - ${response.message()}")
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

// Custom exception for error handling
class BookFetchException(message: String, cause: Throwable? = null) : Exception(message, cause)