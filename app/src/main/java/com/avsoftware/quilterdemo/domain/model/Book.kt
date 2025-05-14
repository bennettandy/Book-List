package com.avsoftware.quilterdemo.domain.model

data class Book(
    val id: String,
    val title: String?,
    val author: String,
    val coverId: Int?,
    val status: BookStatus
){
    enum class CoverSize { SMALL, MEDIUM, LARGE }

    fun getCoverUrl(size: CoverSize = CoverSize.SMALL): String? {
        return coverId?.let {
            when (size) {
                CoverSize.SMALL -> "https://covers.openlibrary.org/b/id/$it-S.jpg"
                CoverSize.MEDIUM -> "https://covers.openlibrary.org/b/id/$it-M.jpg"
                CoverSize.LARGE -> "https://covers.openlibrary.org/b/id/$it-L.jpg"
            }
        }
    }
}