package com.avsoftware.quilterdemo.domain.usecase

import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.repository.BookRepository
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

interface GetBooksUseCase {
    operator fun invoke(): Single<List<Book>>
}