package com.avsoftware.quilterdemo.domain.usecase

import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.repository.BookRepository
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(): Single<List<Book>> {
        return Single.zip(
            bookRepository.getWantToReadList(),
            bookRepository.getCurrentlyReading(),
            bookRepository.getAlreadyRead(),
            { wantToRead, currentlyReading, alreadyRead ->
                wantToRead + currentlyReading + alreadyRead
            }
        ).map { books ->
            books.sortedBy { it.id }
        }.doOnSuccess {
            Timber.d("Got ${it.size} Books")
            // show first few books to check structure
            it.take(6).forEach {
                Timber.d("-- Book $it")
            }
            Timber.d("Got ${it.size} Books")
        }.doOnSuccess {
            Timber.d("Attempting to Load Books")
        }.doOnError {
            Timber.w("Failed to Load books ${it.message}")
        }
    }
}