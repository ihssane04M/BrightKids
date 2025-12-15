package com.example.brightkids

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.brightkids.learning.databinding.ActivityLetterListBinding
import com.example.brightkids.database.AppDatabase
import com.example.brightkids.model.Letter
import com.example.brightkids.DrawingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale



class LetterListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterListBinding
    private lateinit var tts: TextToSpeech
    private lateinit var language: String
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        language = intent.getStringExtra("LANGUAGE") ?: "french"

        setupUI()
        setupRecyclerView()
        initializeTTS()
    }

    private fun setupUI() {
        binding.tvTitle.text = if (language == "arabic") "الحروف العربية" else "L'Alphabet"
        binding.btnBack.setOnClickListener { finish() }
        
        // Setup progress
        updateProgress()
    }

    private fun setupRecyclerView() {
        val letters = if (language == "arabic") getArabicLetters() else getFrenchLetters()
        val adapter = LetterAdapter(letters) { letter ->
            speakLetter(letter)
            openDrawingActivity(letter)
        }
        // Use 5 columns for French, 4 for Arabic (28 letters = 7 rows of 4)
        val columns = if (language == "french") 5 else 4
        binding.recyclerView.layoutManager = GridLayoutManager(this, columns)
        binding.recyclerView.adapter = adapter
    }
    
    private fun updateProgress() {
        lifecycleScope.launch {
            try {
                val letters = if (language == "arabic") getArabicLetters() else getFrenchLetters()
                val totalLetters = letters.size
                
                // Get letter IDs for current language
                val letterIds = letters.map { it.id }
                
                // Get all progress entries for these letters from database
                val progressList = withContext(Dispatchers.IO) {
                    database.letterDao().getProgressForLetters(letterIds)
                }
                
                // Count completed letters (letters with at least 1 star)
                val completedCount = progressList.count { it.stars >= 1 }
                
                // Calculate progress percentage
                val progress = if (totalLetters > 0) {
                    (completedCount * 100) / totalLetters
                } else {
                    0
                }
                
                // Update UI
                binding.progressBar.progress = progress
                binding.tvProgressPercent.text = "$progress%"
                binding.btnProgressCount.text = "$completedCount/$totalLetters"
            } catch (e: Exception) {
                // Handle error - set default values
                val letters = if (language == "arabic") getArabicLetters() else getFrenchLetters()
                val totalLetters = letters.size
                binding.progressBar.progress = 0
                binding.tvProgressPercent.text = "0%"
                binding.btnProgressCount.text = "0/$totalLetters"
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Update progress when returning from DrawingActivity
        updateProgress()
    }

    private fun initializeTTS() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = if (language == "arabic") Locale("ar") else Locale.FRENCH
            }
        }
    }

    private fun speakLetter(letter: Letter) {
        tts.speak(letter.name, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun openDrawingActivity(letter: Letter) {
        val intent = Intent(this, DrawingActivity::class.java)
        intent.putExtra("LETTER", letter.letter)
        intent.putExtra("NAME", letter.name)
        intent.putExtra("LANGUAGE", language)
        intent.putExtra("LETTER_ID", letter.id)
        startActivityForResult(intent, REQUEST_CODE_DRAWING)
    }
    
    companion object {
        private const val REQUEST_CODE_DRAWING = 1001
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DRAWING && resultCode == RESULT_OK) {
            // Progress was updated, refresh the display
            updateProgress()
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

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
