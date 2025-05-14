package com.avsoftware.quilterdemo.data.api

import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.repository.BookRepository
import com.avsoftware.quilterdemo.domain.usecase.GetBooksUseCase
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class GetBooksUseCaseImpl @Inject constructor(private val bookRepository: BookRepository): GetBooksUseCase {
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
                Timber.d("Got ${it.size} Books")
            }
            .doOnSuccess {
                Timber.d("Attempting to Load Books")
            }
            .doOnError {
                Timber.w("Failed to Load books ${it.message}")
            }
    }
}