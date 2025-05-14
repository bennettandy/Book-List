package com.avsoftware.quilterdemo.ui

import android.util.MutableBoolean
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.domain.usecase.GetBooksUseCase
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
    private val getBooksUseCase: GetBooksUseCase
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
        _booksList.subscribeSingle(
            single = getBooksUseCase(),
            disposeBag = disposeBag,
            onSuccess = { _, books -> BookState.Success(books.sortedBy { it.title }) },
            onError = { _, throwable -> BookState.Error(throwable.message ?: "Failed to load books") }
        )
    }

    fun showBottomSheet(book: Book) {
        _selectedBook.value = book
        _showBottomSheet.value = true
    }

    fun hideBottomSheet() {
        _selectedBook.value = null
        _showBottomSheet.value = false
    }

    // This really simplifies state update, extension function on the MutableStateFlow
    // this made more sense when I initially had 3 separate lists
    // keeping as it is more concise
    inline fun <T, S> MutableStateFlow<S>.subscribeSingle(
        single: Single<T>,
        disposeBag: CompositeDisposable,
        crossinline onSuccess: (S, T) -> S,
        crossinline onError: (S, Throwable) -> S
    ) {
        single.subscribeOn(Schedulers.io())
            //.observeOn(AndroidSchedulers.mainThread())
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
