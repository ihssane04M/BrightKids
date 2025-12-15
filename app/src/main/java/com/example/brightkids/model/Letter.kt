package com.example.brightkids.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "letters")
data class Letter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val letter: String,
    val name: String,
    val language: String, // "arabic" or "french"
    val soundUrl: String = ""
)
