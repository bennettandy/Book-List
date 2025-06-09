package com.avsoftware.domain.usecase.impl

import com.avsoftware.domain.model.Book
import com.avsoftware.domain.repository.FlowBookRepository
import com.avsoftware.domain.usecase.GetBooksFlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBooksUseCaseFlowImpl @Inject constructor(private val bookRepository: FlowBookRepository) :
    GetBooksFlowUseCase {
    override suspend fun invoke(): Flow<List<Book>> {
        return combine(
            bookRepository.getWantToReadList(),
            bookRepository.getCurrentlyReading(),
            bookRepository.getAlreadyRead()
        ) { wantToRead, currentlyReading, alreadyRead ->
            wantToRead + currentlyReading + alreadyRead
        }
            .map {
                // remove any empty items
                it.filter { !it.title.isNullOrEmpty() }
            }
            .map { books ->
                books.sortedBy { it.id }
            }
    }
}
