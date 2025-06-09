package com.avsoftware.domain.usecase

import com.avsoftware.domain.model.Book
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface GetBooksUseCase {
    operator fun invoke(): Single<List<Book>>
}

interface GetBooksFlowUseCase {
    suspend operator fun invoke(): Flow<List<Book>>
}