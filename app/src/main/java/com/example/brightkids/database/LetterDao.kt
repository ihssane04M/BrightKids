package com.example.brightkids.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.brightkids.model.Letter
import com.example.brightkids.model.LetterProgress

@Dao
interface LetterDao {
    @Query("SELECT * FROM letters WHERE language = :language")
    fun getLettersByLanguage(language: String): LiveData<List<Letter>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertLetter(letter: Letter)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProgress(progress: LetterProgress)

    @Query("SELECT * FROM progress WHERE letterId = :letterId")
    fun getProgress(letterId: Int): LiveData<LetterProgress?>

    @Query("UPDATE progress SET practiceCount = practiceCount + 1 WHERE letterId = :letterId")
    suspend fun incrementPractice(letterId: Int)
    
    @Query("UPDATE progress SET stars = :stars WHERE letterId = :letterId AND stars < :stars")
    suspend fun updateStarsIfBetter(letterId: Int, stars: Int)
    
    // Update stars for a letter (replace if better, or always update)
    @Query("UPDATE progress SET stars = :stars WHERE letterId = :letterId")
    suspend fun updateStars(letterId: Int, stars: Int)
    
    @Query("INSERT OR IGNORE INTO progress (letterId, practiceCount, stars) VALUES (:letterId, 1, :stars)")
    suspend fun insertProgressIfNotExists(letterId: Int, stars: Int)
    
    // Get all progress entries
    @Query("SELECT * FROM progress")
    suspend fun getAllProgress(): List<LetterProgress>
    
    // Count completed letters (letters with at least 1 star)
    @Query("SELECT COUNT(*) FROM progress WHERE stars >= 1")
    suspend fun getCompletedLettersCount(): Int
    
    // Get progress for multiple letter IDs
    @Query("SELECT * FROM progress WHERE letterId IN (:letterIds)")
    suspend fun getProgressForLetters(letterIds: List<Int>): List<LetterProgress>
}