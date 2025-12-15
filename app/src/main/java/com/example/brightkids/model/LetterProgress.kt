package com.example.brightkids.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class LetterProgress(
    @PrimaryKey
    val letterId: Int,
    val practiceCount: Int = 0,
    val stars: Int = 0
)