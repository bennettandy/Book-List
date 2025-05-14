package com.avsoftware.quilterdemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookScreen(viewModel: BookViewModel, modifier: Modifier) {
    val bookList = viewModel.booksList.collectAsState().value

    when (bookList) {
        is BookState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is BookState.Success -> {
            LazyColumn {
                itemsIndexed(bookList.books) { index, book ->
                    //key { book.id }
                    BookItem(book = book)
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

    Button(
        onClick = { viewModel.loadBooks() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Refresh")
    }

}