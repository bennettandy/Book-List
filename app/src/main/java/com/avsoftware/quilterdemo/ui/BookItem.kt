package com.avsoftware.quilterdemo.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.avsoftware.quilterdemo.domain.model.Book

@Composable
fun BookItem(book: Book){
    Row {
        Text("Title ${book.title}")
    }
}
