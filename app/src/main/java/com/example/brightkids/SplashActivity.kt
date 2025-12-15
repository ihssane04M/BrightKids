package com.example.brightkids

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.brightkids.learning.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val prefsManager by lazy { PrefsManager.getInstance(this) }
    
    companion object {
        private const val SPLASH_DELAY = 2000L // 2 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start fade-in animation with scale and translation
        binding.logoImageView.alpha = 0f
        binding.logoImageView.scaleX = 0.8f
        binding.logoImageView.scaleY = 0.8f
        binding.logoImageView.translationY = -30f
        
        binding.titleTextView.alpha = 0f
        binding.titleTextView.translationY = 20f
        
        binding.logoImageView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .translationY(0f)
            .setDuration(900)
            .setStartDelay(100)
            .start()
        
        binding.titleTextView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(400)
            .start()

        // Navigate after delay
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DELAY)
    }

    private fun navigateToNextScreen() {
        val intent = when {
            prefsManager.isOnboardingCompleted() -> {
                Intent(this, MainActivity::class.java)
            }
            !prefsManager.isIntroCompleted() -> {
                Intent(this, IntroActivity::class.java)
            }
            else -> {
                Intent(this, OnboardingActivity::class.java)
            }
        }
        
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}

