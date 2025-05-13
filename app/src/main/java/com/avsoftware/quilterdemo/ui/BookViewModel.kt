package com.avsoftware.quilterdemo.ui

import androidx.lifecycle.ViewModel
import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.usecase.GetAlreadyReadUseCase
import com.avsoftware.quilterdemo.domain.usecase.GetCurrentlyReadingUseCase
import com.avsoftware.quilterdemo.domain.usecase.GetWantToReadBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getWantToReadBooksUseCase: GetWantToReadBooksUseCase,
    private val getCurrentlyReadingUseCase: GetCurrentlyReadingUseCase,
    private val getAlreadyReadUseCase: GetAlreadyReadUseCase
) : ViewModel() {

    private val _wantToReadBooks = MutableStateFlow<List<Book>>(emptyList())
    val wantToReadBooks: StateFlow<List<Book>> = _wantToReadBooks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val disposeBag = CompositeDisposable()

    init {
        loadWantToReadBooks()
    }

    fun loadWantToReadBooks() {
        _isLoading.value = true
        getWantToReadBooksUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { books ->
                    _wantToReadBooks.value = books.sortedBy { it.title } // Business logic
                    _isLoading.value = false
                    _error.value = null
                },
                { throwable ->
                    _error.value = throwable.message ?: "Failed to load books"
                    _isLoading.value = false
                }
            ).apply {
                // keep the disposable for cleanup
                disposeBag.add(this)
            }
    }

    override fun onCleared() {
        disposeBag.clear()
        super.onCleared()
    }
}