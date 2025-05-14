package com.avsoftware.quilterdemo.domain.model

data class Book(
    val id: String,
    val title: String?,
    val author: String,
    val coverId: Int?,
    val status: BookStatus
){
    fun getCoverUrl(): String? = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
}