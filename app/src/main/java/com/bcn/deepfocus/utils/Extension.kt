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

fun Int.toTimerText(): String {
    val minutes = this / 60
    val seconds = this % 60

    return "%02d:%02d".format(
        minutes,
        seconds
    )
}

fun Int.toSecond(): Int {
    return this * 60
}