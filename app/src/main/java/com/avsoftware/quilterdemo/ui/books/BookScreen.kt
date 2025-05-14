package com.avsoftware.quilterdemo.ui.books

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.avsoftware.quilterdemo.domain.model.Book
import com.avsoftware.quilterdemo.ui.BookState

@Composable
fun BookScreen(
    bookList: BookState,
    modifier: Modifier,
    bookClicked: (Book) -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {
        when (bookList) {
            is BookState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is BookState.Success -> {
                LazyColumn {
                    itemsIndexed(bookList.books) { index, book ->
                        //key { book.id }
                        BookItem(book = book, modifier = Modifier.clickable(
                            onClick = { bookClicked(book) }
                        ))
                    }
                }
            }

            is BookState.Error -> {
                Text(
                    text = bookList.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}