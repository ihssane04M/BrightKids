package com.example.brightkids

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brightkids.learning.databinding.ActivityDrawingBinding
import com.brightkids.learning.databinding.DialogScoreBinding
import com.example.brightkids.database.AppDatabase
import com.example.brightkids.model.Letter
import com.example.brightkids.model.LetterProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class DrawingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding
    private lateinit var tts: TextToSpeech
    private var letterName = ""
    private var language = ""
    private var letterId: Int = 0
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val letter = intent.getStringExtra("LETTER") ?: "A"
        letterName = intent.getStringExtra("NAME") ?: "A"
        language = intent.getStringExtra("LANGUAGE") ?: "french"
        letterId = intent.getIntExtra("LETTER_ID", 0)

        setupUI(letter)
        initializeTTS()
        setupScoreTracking()
    }

    private fun setupUI(letter: String) {
        binding.tvLetter.text = letter
        binding.tvLetterName.text = letterName
        
        // Set the letter guide in the drawing view
        binding.drawingView.setLetter(letter)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnPlaySound.setOnClickListener { speakLetter() }
        binding.btnClear.setOnClickListener { binding.drawingView.clear() }
        binding.btnRestart.setOnClickListener {
            binding.drawingView.clear()
            speakLetter()
        }
    }

    private fun initializeTTS() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = if (language == "arabic") Locale("ar") else Locale.FRENCH
                tts.language = locale
                speakLetter()
            }
        }
    }

    private fun setupScoreTracking() {
        binding.drawingView.onDrawingComplete = { score, duration ->
            val stars = binding.drawingView.getStarsFromScore(score)
            showScoreDialog(score, stars)
            saveProgress(score, stars)
        }
    }
    
    private fun showScoreDialog(score: Int, stars: Int) {
        val dialogBinding = DialogScoreBinding.inflate(LayoutInflater.from(this))
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false) // Prevent dismissing by clicking outside
        
        // Set score
        dialogBinding.tvScoreValue.text = "$score%"
        
        // Set dynamic title and message based on stars
        val (title, message) = getScoreMessages(stars, score)
        dialogBinding.tvScoreTitle.text = title
        dialogBinding.tvScoreMessage.text = message
        
        // Initialize stars with low alpha
        dialogBinding.star1.alpha = 0.3f
        dialogBinding.star2.alpha = 0.3f
        dialogBinding.star3.alpha = 0.3f
        dialogBinding.star1.scaleX = 0.5f
        dialogBinding.star1.scaleY = 0.5f
        dialogBinding.star2.scaleX = 0.5f
        dialogBinding.star2.scaleY = 0.5f
        dialogBinding.star3.scaleX = 0.5f
        dialogBinding.star3.scaleY = 0.5f
        
        // Animate stars based on score with delay
        animateStars(dialogBinding, stars)
        
        // Setup button click listeners
        dialogBinding.btnScoreOk.setOnClickListener {
            dialog.dismiss()
            // Always navigate to next letter when clicking continue
            navigateToNextLetter()
        }
        
        dialogBinding.btnScoreTryAgain.setOnClickListener {
            dialog.dismiss()
            // Clear and restart current letter
            binding.drawingView.clear()
            speakLetter()
        }
        
        dialog.show()
    }
    
    private fun navigateToNextLetter() {
        val nextLetter = getNextLetter()
        if (nextLetter != null) {
            val intent = Intent(this, DrawingActivity::class.java)
            intent.putExtra("LETTER", nextLetter.letter)
            intent.putExtra("NAME", nextLetter.name)
            intent.putExtra("LANGUAGE", language)
            intent.putExtra("LETTER_ID", nextLetter.id)
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            // No next letter, go back to letter list with result to refresh progress
            setResult(RESULT_OK)
            finish()
        }
    }
    
    override fun onBackPressed() {
        // When going back, set result to update progress in LetterListActivity
        setResult(RESULT_OK)
        super.onBackPressed()
    }
    
    private fun getNextLetter(): Letter? {
        val letters = if (language == "arabic") getArabicLetters() else getFrenchLetters()
        val currentIndex = letters.indexOfFirst { it.id == letterId }
        
        return if (currentIndex >= 0 && currentIndex < letters.size - 1) {
            letters[currentIndex + 1]
        } else {
            null // Last letter, no next letter
        }
    }
    
    private fun getArabicLetters(): List<Letter> = listOf(
        Letter(1, "ا", "Alif", "arabic"),
        Letter(2, "ب", "Ba", "arabic"),
        Letter(3, "ت", "Ta", "arabic"),
        Letter(4, "ث", "Tha", "arabic"),
        Letter(5, "ج", "Jim", "arabic"),
        Letter(6, "ح", "Ha", "arabic"),
        Letter(7, "خ", "Kha", "arabic"),
        Letter(8, "د", "Dal", "arabic"),
        Letter(9, "ذ", "Dhal", "arabic"),
        Letter(10, "ر", "Ra", "arabic"),
        Letter(11, "ز", "Zay", "arabic"),
        Letter(12, "س", "Sin", "arabic"),
        Letter(13, "ش", "Shin", "arabic"),
        Letter(14, "ص", "Sad", "arabic"),
        Letter(15, "ض", "Dad", "arabic"),
        Letter(16, "ط", "Ta", "arabic"),
        Letter(17, "ظ", "Za", "arabic"),
        Letter(18, "ع", "Ain", "arabic"),
        Letter(19, "غ", "Ghain", "arabic"),
        Letter(20, "ف", "Fa", "arabic"),
        Letter(21, "ق", "Qaf", "arabic"),
        Letter(22, "ك", "Kaf", "arabic"),
        Letter(23, "ل", "Lam", "arabic"),
        Letter(24, "م", "Mim", "arabic"),
        Letter(25, "ن", "Nun", "arabic"),
        Letter(26, "ه", "Ha", "arabic"),
        Letter(27, "و", "Waw", "arabic"),
        Letter(28, "ي", "Ya", "arabic")
    )
    
    private fun getFrenchLetters(): List<Letter> = listOf(
        Letter(1, "A", "A", "french"),
        Letter(2, "B", "Bé", "french"),
        Letter(3, "C", "Cé", "french"),
        Letter(4, "D", "Dé", "french"),
        Letter(5, "E", "E", "french"),
        Letter(6, "F", "Effe", "french"),
        Letter(7, "G", "Gé", "french"),
        Letter(8, "H", "Hache", "french"),
        Letter(9, "I", "I", "french"),
        Letter(10, "J", "Ji", "french"),
        Letter(11, "K", "Ka", "french"),
        Letter(12, "L", "Elle", "french"),
        Letter(13, "M", "Emm", "french"),
        Letter(14, "N", "Enn", "french"),
        Letter(15, "O", "O", "french"),
        Letter(16, "P", "Pé", "french"),
        Letter(17, "Q", "Qu", "french"),
        Letter(18, "R", "Erre", "french"),
        Letter(19, "S", "Ess", "french"),
        Letter(20, "T", "Té", "french"),
        Letter(21, "U", "U", "french"),
        Letter(22, "V", "Vé", "french"),
        Letter(23, "W", "Double vé", "french"),
        Letter(24, "X", "Ixe", "french"),
        Letter(25, "Y", "I grec", "french"),
        Letter(26, "Z", "Zède", "french")
    )
    
    private fun getScoreMessages(stars: Int, score: Int): Pair<String, String> {
        return when (stars) {
            3 -> Pair("Excellent ! ⭐⭐⭐", "Parfait ! Tu es incroyable !")
            2 -> Pair("Très bien ! ⭐⭐", "Super travail ! Tu progresses bien !")
            1 -> Pair("Bien joué ! ⭐", "Continue tes efforts, tu y es presque !")
            else -> Pair("Continue ! 💪", "N'abandonne pas, tu peux y arriver !")
        }
    }
    
    private fun animateStars(binding: DialogScoreBinding, stars: Int) {
        // Animate stars one by one with a bounce effect
        val starsList = listOf(binding.star1, binding.star2, binding.star3)
        
        starsList.forEachIndexed { index, starView ->
            val delay = index * 200L // 200ms delay between each star
            val hasStar = stars > index
            
            starView.postDelayed({
                if (hasStar) {
                    // Bounce animation for earned stars
                    starView.animate()
                        .alpha(1f)
                        .scaleX(1.2f)
                        .scaleY(1.2f)
                        .setDuration(300)
                        .withEndAction {
                            starView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(200)
                                .start()
                        }
                        .start()
                } else {
                    // Fade in for non-earned stars
                    starView.animate()
                        .alpha(0.3f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
            }, delay)
        }
    }
    
    private fun saveProgress(score: Int, stars: Int) {
        if (letterId == 0) return
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // First ensure entry exists (creates if doesn't exist)
                database.letterDao().insertProgressIfNotExists(letterId, stars)
                
                // Increment practice count
                database.letterDao().incrementPractice(letterId)
                
                // Update stars if this attempt is better (for progression, we want to keep best score)
                // Important: A letter is considered "completed" for progression if it has at least 1 star
                if (stars > 0) {
                    // Update stars only if new stars are better than current
                    database.letterDao().updateStarsIfBetter(letterId, stars)
                    
                    // Ensure minimum 1 star is saved for progression tracking
                    // If updateStarsIfBetter didn't update (because current was higher),
                    // we still need to make sure the entry exists with at least 1 star
                    database.letterDao().insertProgress(
                        LetterProgress(letterId, 1, stars.coerceAtLeast(1))
                    )
                }
            } catch (e: Exception) {
                // Fallback: simple insert
                try {
                    database.letterDao().insertProgress(
                        LetterProgress(letterId, 1, stars.coerceAtLeast(0))
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun speakLetter() {
        tts.speak(letterName, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}