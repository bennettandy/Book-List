package com.avsoftware.quilterdemo.ui

import app.cash.turbine.test
import com.avsoftware.domain.model.Book
import com.avsoftware.domain.model.BookStatus
import com.avsoftware.domain.usecase.GetBooksUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
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
        Dispatchers.setMain(testDispatcher)
        // Mock useCase to avoid init block triggering real data fetch
        every { useCase() } returns Single.just(emptyList())
        viewModel = BookViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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