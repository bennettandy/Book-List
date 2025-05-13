package com.avsoftware.quilterdemo.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BookScreen(viewModel: BookViewModel, modifier: Modifier){
    Text("View Model $viewModel")
}