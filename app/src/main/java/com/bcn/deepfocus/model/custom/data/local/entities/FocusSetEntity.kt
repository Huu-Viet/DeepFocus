package com.bcn.deepfocus.model.custom.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "focus_set",
)
data class FocusSetEntity(
    @PrimaryKey
    val id: String,
    val focusMinute: Int,
    val breakMinute: Int,
)
