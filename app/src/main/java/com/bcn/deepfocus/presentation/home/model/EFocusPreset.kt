package com.bcn.deepfocus.presentation.home.model

enum class EFocusPreset(
    val focusMinutes: Int,
    val breakMinutes: Int,
) {
    DEFAULT(25, 5),
    LONG(50, 10),
    DEEP(90, 15)
}