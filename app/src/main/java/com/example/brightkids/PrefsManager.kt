package com.example.brightkids

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "brightkids_prefs"
        private const val KEY_INTRO_COMPLETED = "intro_completed"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_AVATAR = "user_avatar"
        
        @Volatile
        private var INSTANCE: PrefsManager? = null

        fun getInstance(context: Context): PrefsManager {
            return INSTANCE ?: synchronized(this) {
                val instance = PrefsManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "") ?: ""
    }

    fun setUserAvatar(avatarId: Int) {
        prefs.edit().putInt(KEY_USER_AVATAR, avatarId).apply()
    }

    fun getUserAvatar(): Int {
        return prefs.getInt(KEY_USER_AVATAR, 0)
    }

    fun setIntroCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_INTRO_COMPLETED, completed).apply()
    }

    fun isIntroCompleted(): Boolean {
        return prefs.getBoolean(KEY_INTRO_COMPLETED, false)
    }
}

