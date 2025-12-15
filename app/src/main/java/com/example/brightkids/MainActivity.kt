package com.example.brightkids


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brightkids.learning.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnArabic.setOnClickListener {
            startLetterActivity("arabic")
        }

        binding.btnFrench.setOnClickListener {
            startLetterActivity("french")
        }
    }

    private fun startLetterActivity(language: String) {
        val intent = Intent(this, LetterListActivity::class.java)
        intent.putExtra("LANGUAGE", language)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}