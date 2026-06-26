package com.bcn.deepfocus.presentation.custom.model

data class CustomTimerUIState(
    val focusMinute: Int = 25,
    val breakMinute: Int = 5,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val warningMessage: String? = null
)
