package com.avsoftware.quilterdemo.domain.model

sealed interface BookStatus {
    data object WantToRead: BookStatus
    data object CurrentlyReading: BookStatus
    data object AlreadyRead: BookStatus
}