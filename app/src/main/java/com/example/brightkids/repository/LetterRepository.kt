package com.example.brightkids.repository

import androidx.lifecycle.LiveData
import com.example.brightkids.database.LetterDao
import com.example.brightkids.model.Letter
import com.example.brightkids.model.LetterProgress

class LetterRepository(private val letterDao: LetterDao) {

    fun getLettersByLanguage(language: String): LiveData<List<Letter>> {
        return letterDao.getLettersByLanguage(language)
    }

    suspend fun insertLetter(letter: Letter) {
        letterDao.insertLetter(letter)
    }

    suspend fun updateProgress(letterId: Int) {
        letterDao.incrementPractice(letterId)
    }

    fun getProgress(letterId: Int): LiveData<LetterProgress?> {
        return letterDao.getProgress(letterId)
    }
}
