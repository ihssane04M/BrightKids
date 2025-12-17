package com.example.brightkids

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.brightkids.learning.databinding.ActivityLetterListBinding
import com.example.brightkids.model.Letter
import java.util.Locale

class LetterListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterListBinding
    private lateinit var tts: TextToSpeech
    private lateinit var language: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        language = intent.getStringExtra("LANGUAGE") ?: "french"

        applyLayoutDirection()
        setupUI()
        setupRecyclerView()
        initializeTTS()
    }

    private fun applyLayoutDirection() {
        val direction =
            if (language == "arabic") ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR

        ViewCompat.setLayoutDirection(binding.root, direction)
        ViewCompat.setLayoutDirection(binding.header, direction)
        ViewCompat.setLayoutDirection(binding.recyclerView, direction)

        binding.tvTitle.textDirection =
            if (language == "arabic") View.TEXT_DIRECTION_RTL else View.TEXT_DIRECTION_LTR
    }

    private fun setupUI() {
        binding.tvTitle.text = if (language == "arabic") "الحروف العربية" else "L'Alphabet"
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        val letters = if (language == "arabic") getArabicLetters() else getFrenchLetters()
        val adapter = LetterAdapter(letters) { letter ->
            speakLetter(letter)
            openDrawingActivity(letter)
        }
        // Use 2 columns (both languages)
        val columns = 2
        binding.recyclerView.layoutManager = GridLayoutManager(this, columns)
        binding.recyclerView.adapter = adapter
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
        startActivity(intent)
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
        Letter(16, "ط", "Tae", "arabic"),
        Letter(17, "ظ", "Za", "arabic"),
        Letter(18, "ع", "Ain", "arabic"),
        Letter(19, "غ", "Ghain", "arabic"),
        Letter(20, "ف", "Fa", "arabic"),
        Letter(21, "ق", "Qaf", "arabic"),
        Letter(22, "ك", "Kaf", "arabic"),
        Letter(23, "ل", "Lam", "arabic"),
        Letter(24, "م", "Mim", "arabic"),
        Letter(25, "ن", "Nun", "arabic"),
        Letter(26, "ه", "Hae", "arabic"),
        Letter(27, "و", "Waw", "arabic"),
        Letter(28, "ي", "Yae", "arabic")
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
        Letter(11, "K", "Ka",  "french"),
        Letter(12, "L", "Elle", "french"),
        Letter(13, "M", "Em", "french"),
        Letter(14, "N", "En", "french"),
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
