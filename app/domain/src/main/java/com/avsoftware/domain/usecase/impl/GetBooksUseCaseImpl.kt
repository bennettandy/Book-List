package com.avsoftware.domain.usecase.impl

import com.avsoftware.domain.model.Book
import com.avsoftware.domain.repository.BookRepository
import com.avsoftware.domain.usecase.GetBooksUseCase
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

/**
 * Use Case Implementation can live in domain layer as it only has
 * dependencies to Domain objects
 */
class GetBooksUseCaseImpl @Inject constructor(private val bookRepository: BookRepository):
    GetBooksUseCase {
    override operator fun invoke(): Single<List<Book>> {

        // Zip the 3 endpoints into a Single List
        return Single.zip(
            bookRepository.getWantToReadList(),
            bookRepository.getCurrentlyReading(),
            bookRepository.getAlreadyRead(),
            { wantToRead, currentlyReading, alreadyRead ->
                wantToRead + currentlyReading + alreadyRead
            }
        )
            .map {
                // remove any empty items
                it.filter { !it.title.isNullOrEmpty() }
            }
            .map { books ->
                books.sortedBy { it.id }
            }
            .doOnSuccess {
                Timber.Forest.d("Got ${it.size} Books")
            }
            .doOnSuccess {
                Timber.Forest.d("Attempting to Load Books")
            }
            .doOnError {
                Timber.Forest.w("Failed to Load books ${it.message}")
            }
    }
}