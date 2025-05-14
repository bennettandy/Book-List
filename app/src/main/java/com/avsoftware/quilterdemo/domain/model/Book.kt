package com.avsoftware.quilterdemo.domain.model

data class Book(
    val id: String,
    val title: String?,
    val author: String,
    val status: BookStatus
)