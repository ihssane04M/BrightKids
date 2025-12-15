package com.example.brightkids.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.brightkids.model.Letter
import com.example.brightkids.model.LetterProgress

@Database(entities = [Letter::class, LetterProgress::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun letterDao(): LetterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kids_learning_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
