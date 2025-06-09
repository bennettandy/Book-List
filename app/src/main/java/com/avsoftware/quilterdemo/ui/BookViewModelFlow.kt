package com.avsoftware.quilterdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avsoftware.domain.model.Book
import com.avsoftware.domain.usecase.GetBooksFlowUseCase
import com.avsoftware.domain.usecase.GetBooksUseCase
import com.avsoftware.domain.usecase.impl.GetBooksUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModelFlow @Inject constructor(
    private val getBooksUseCase: GetBooksFlowUseCase
) : ViewModel() {

    private val _booksList = MutableStateFlow<BookState>(BookState.Loading)
    val booksList: StateFlow<BookState> = _booksList.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()

    private val disposeBag = CompositeDisposable()

    init {
        // trigger in init for simplicity, we can refactor if needed
        loadBooks()
    }

    fun loadBooks() {
        _booksList.value = BookState.Loading

        viewModelScope.launch {
            getBooksUseCase()
                .catch { throwable ->
                    _booksList.value = BookState.Error(throwable.message ?: "Failed to load books")
                }
                .collect { books ->
                    _booksList.value = BookState.Success(books.sortedBy { it.title })
                }
        }
    }

    fun showBottomSheet(book: Book) {
        _selectedBook.value = book
        _showBottomSheet.value = true
    }

    fun hideBottomSheet() {
        _selectedBook.value = null
        _showBottomSheet.value = false
    }

    override fun onCleared() {
        disposeBag.clear()
        super.onCleared()
    }
}
