package com.avsoftware.quilterdemo.ui

import androidx.lifecycle.ViewModel
import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.usecase.GetAlreadyReadUseCase
import com.avsoftware.quilterdemo.domain.usecase.GetCurrentlyReadingUseCase
import com.avsoftware.quilterdemo.domain.usecase.GetWantToReadBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
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

    private val _wantToRead = MutableStateFlow<BookState>(BookState.Loading)
    val wantToRead: StateFlow<BookState> = _wantToRead.asStateFlow()

    private val _currentlyReading = MutableStateFlow<BookState>(BookState.Loading)
    val currentlyReading: StateFlow<BookState> = _currentlyReading.asStateFlow()

    private val _alreadyRead = MutableStateFlow<BookState>(BookState.Loading)
    val alreadyRead: StateFlow<BookState> = _alreadyRead.asStateFlow()

    private val disposeBag = CompositeDisposable()

    init {
        // todo: trigger this from the UI
        loadWantToReadBooks()
        loadCurrentlyReading()
        loadAlreadyRead()
    }

    fun loadWantToReadBooks() {
        _wantToRead.subscribeSingle(
            single = getWantToReadBooksUseCase(),
            disposeBag = disposeBag,
            onSuccess = { _, books -> BookState.Success(books.sortedBy { it.title }) },
            onError = { _, throwable -> BookState.Error(throwable.message ?: "Failed to load books") }
        )
    }

    fun loadCurrentlyReading() {
        _currentlyReading.subscribeSingle(
            single = getCurrentlyReadingUseCase(),
            disposeBag = disposeBag,
            onSuccess = { _, books -> BookState.Success(books.sortedBy { it.title }) },
            onError = { _, throwable -> BookState.Error(throwable.message ?: "Failed to load books") }
        )
    }

    fun loadAlreadyRead() {
        _alreadyRead.subscribeSingle(
            single = getAlreadyReadUseCase(),
            disposeBag = disposeBag,
            onSuccess = { _, books -> BookState.Success(books.sortedBy { it.title }) },
            onError = { _, throwable -> BookState.Error(throwable.message ?: "Failed to load books") }
        )
    }

    // This really simplifies state update, extension function on the MutableStateFlow
    inline fun <T, S> MutableStateFlow<S>.subscribeSingle(
        single: Single<T>,
        disposeBag: CompositeDisposable,
        crossinline onSuccess: (S, T) -> S,
        crossinline onError: (S, Throwable) -> S
    ) {
        single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // set the Success State
                { result -> this.value = onSuccess(this.value, result) },
                // set the Error State
                { error -> this.value = onError(this.value, error) }
            )
            .also { disposeBag.add(it) }
    }

    override fun onCleared() {
        disposeBag.clear()
        super.onCleared()
    }
}

sealed class BookState {
    data object Loading : BookState()
    data class Success(val books: List<Book>) : BookState()
    data class Error(val message: String) : BookState()
}
