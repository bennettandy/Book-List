package com.avsoftware.quilterdemo.domain.common

import com.avsoftware.core.DomainError

// Rx Means we don't need This
sealed class DomainResult<out D, out E: DomainError> {
    data class Success<out D>(val data: D): DomainResult<D, Nothing>()
    data class Error<out E: DomainError>(val error: E): DomainResult<Nothing, E>()
}