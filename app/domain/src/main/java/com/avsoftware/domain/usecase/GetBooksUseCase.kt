package com.avsoftware.domain.usecase

import com.avsoftware.domain.model.Book
import io.reactivex.Single

interface GetBooksUseCase {
    operator fun invoke(): Single<List<Book>>
}