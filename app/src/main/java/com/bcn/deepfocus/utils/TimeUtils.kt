package com.bcn.deepfocus.utils

fun formatDuration(totalSeconds: Int): String {
    if (totalSeconds <= 0) return "0s"

    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60

    val result = StringBuilder()

    if (h > 0) {
        result.append("${h}h")
    }

    if (m > 0) {
        if (result.isNotEmpty()) result.append(" ")
        result.append("${m}m")
    }

    if (s > 0) {
        if (result.isNotEmpty()) result.append(" ")
        result.append("${s}s")
    }

    return result.toString().trim()
}