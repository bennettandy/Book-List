package com.avsoftware.quilterdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.avsoftware.quilterdemo.ui.BookState
import com.avsoftware.quilterdemo.ui.books.BookScreen
import com.avsoftware.quilterdemo.ui.BookViewModel
import com.avsoftware.quilterdemo.ui.theme.QuilterDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: BookViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuilterDemoTheme {

                val bookList = viewModel.booksList.collectAsState().value
                val showBottomSheet = viewModel.showBottomSheet.collectAsState().value

                val bottomSheetState = rememberModalBottomSheetState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        Button(
                            enabled = bookList !is BookState.Loading,
                            onClick = viewModel::loadBooks,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Refresh")
                        }
                    }
                ) { innerPadding ->
                    BookScreen(
                        bookList = bookList,
                        modifier = Modifier.padding(innerPadding),
                        bookClicked = { viewModel.showBottomSheet() }
                    )

                    // Bottom Sheet
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { viewModel.hideBottomSheet() },
                            sheetState = bottomSheetState,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(){
                                Text("One")
                                Text("Two")
                            }
//                            BottomSheetContent(
//                                onRefresh = {
//                                    viewModel.loadBooks()
//                                    coroutineScope.launch { bottomSheetState.hide() }
//                                    showBottomSheet = false
//                                },
//                                onCancel = {
//                                    coroutineScope.launch { bottomSheetState.hide() }
//                                    showBottomSheet = false
//                                }
//                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuilterDemoTheme {
        Greeting("Android")
    }
}