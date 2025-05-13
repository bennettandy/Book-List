package com.avsoftware.quilterdemo.domain.usecase

import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.repository.BookRepository
import io.reactivex.Single
import javax.inject.Inject

class GetWantToReadBooksUseCase @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(): Single<List<Book>> = bookRepository.getWantToReadList()
}