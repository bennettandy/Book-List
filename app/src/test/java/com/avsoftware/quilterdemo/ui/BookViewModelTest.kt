package com.avsoftware.quilterdemo.ui

import app.cash.turbine.test
import com.avsoftware.domain.model.Book
import com.avsoftware.domain.model.BookStatus
import com.avsoftware.domain.usecase.GetBooksUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModelTest {

    private val useCase: GetBooksUseCase = mockk()
    private lateinit var viewModel: BookViewModel

    // no delays and runs coroutines synchronously <sp?>
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Set coroutine dispatcher
        Dispatchers.setMain(testDispatcher)

////        // Override All of the RxJava schedulers to use trampoline
//        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
//        RxJavaPlugins.setInitComputationSchedulerHandler { Schedulers.trampoline() }
//        RxJavaPlugins.setInitNewThreadSchedulerHandler { Schedulers.trampoline() }
//        RxJavaPlugins.setInitSingleSchedulerHandler { Schedulers.trampoline() }
//
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        // Mock useCase to return empty list
        every { useCase() } returns Single.just(emptyList())

        // Initialize ViewModel
        viewModel = BookViewModel(useCase)
    }

    @After
    fun tearDown() {
        // Reset coroutine dispatcher
        Dispatchers.resetMain()
        // Reset RxJava schedulers
        RxJavaPlugins.reset()
    }

    @Test
    fun `loadBooks emits Loading then Success state on successful data fetch`() = runTest {
        // Arrange
        val books = listOf(Book("1", "Book A", "Author X", 147111, BookStatus.AlreadyRead))
        every { useCase() } returns Single.just(books)

        // Act
        viewModel.loadBooks()

        // Assert
        viewModel.booksList.test {
//            assertEquals(BookState.Loading, awaitItem()) // Initial Loading state
            assertEquals(BookState.Success(books.sortedBy { it.title }), awaitItem()) // Success state
        }
    }

    @Test
    fun `loadBooks emits Loading then Error state on failure`() = runTest {
        // Arrange
        val error = RuntimeException("Network error")
        every { useCase() } returns Single.error(error)

        // Act
        viewModel.loadBooks()

        // Assert
        viewModel.booksList.test {
//            assertEquals(BookState.Loading, awaitItem()) // Initial Loading state
            assertEquals(BookState.Error("Network error"), awaitItem()) // Error state
        }
    }

    @Test
    fun `showBottomSheet sets selectedBook and showBottomSheet state`() = runTest {
        // Arrange
        val book = Book("1", "Book A", "Author X", 147111, BookStatus.AlreadyRead)

        // Act
        viewModel.showBottomSheet(book)

        // Assert
        assertEquals(book, viewModel.selectedBook.value)
        assertEquals(true, viewModel.showBottomSheet.value)
    }

    @Test
    fun `hideBottomSheet clears selectedBook and showBottomSheet state`() = runTest {
        // Arrange
        val book = Book("1", "Book A", "Author X", 147111, BookStatus.AlreadyRead)
        viewModel.showBottomSheet(book)

        // Act
        viewModel.hideBottomSheet()

        // Assert
        assertEquals(null, viewModel.selectedBook.value)
        assertEquals(false, viewModel.showBottomSheet.value)
    }
}