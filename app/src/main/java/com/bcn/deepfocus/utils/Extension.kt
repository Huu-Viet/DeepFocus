package com.bcn.deepfocus.utils

import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<Response<T>>.collectAndHandle(
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit,
    onLoading: () -> Unit
) {
    collect { response ->
        when (response) {
            is Response.Success -> onSuccess(response.data)
            is Response.Error -> onError(response.message)
            is Response.Loading -> onLoading()
        }
    }
}