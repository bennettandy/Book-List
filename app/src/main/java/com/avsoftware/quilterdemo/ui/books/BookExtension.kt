package com.avsoftware.quilterdemo.ui.books

import com.avsoftware.domain.model.Book

// This is UI code so having it as an extension keeps the Domain model cleaner
enum class CoverSize { SMALL, MEDIUM, LARGE }

fun Book.getCoverUrl(size: CoverSize = CoverSize.SMALL): String? {
    return coverId?.let {
        when (size) {
            CoverSize.SMALL -> "https://covers.openlibrary.org/b/id/$it-S.jpg"
            CoverSize.MEDIUM -> "https://covers.openlibrary.org/b/id/$it-M.jpg"
            CoverSize.LARGE -> "https://covers.openlibrary.org/b/id/$it-L.jpg"
        }
    }
}