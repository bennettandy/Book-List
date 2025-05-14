package com.avsoftware.quilterdemo.ui.books

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.avsoftware.quilterdemo.R
import com.avsoftware.quilterdemo.domain.model.Book

@Composable
fun DetailsBottomSheet(
    book: Book,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = book.title.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Large Cover Image
        book.getCoverUrl(CoverSize.MEDIUM)?.let { url ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.ic_menu_gallery)
                    .build(),
                contentDescription = stringResource(id = R.string.cover_of, book.title.orEmpty()),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 200.dp, height = 300.dp)
                    .padding(vertical = 8.dp)
            )
        } ?: Text(
            text = stringResource(R.string.no_cover_available),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Author
        Text(
            text = stringResource(id = R.string.by_author, book.author),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Buttons
        OutlinedButton(
            onClick = onDoneClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.done_button))
        }
    }
}