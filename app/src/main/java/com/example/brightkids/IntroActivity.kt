package com.example.brightkids

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.brightkids.learning.databinding.ActivityIntroBinding
import com.brightkids.learning.databinding.ItemIntroPageBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private val prefsManager by lazy { PrefsManager.getInstance(this) }

    private val introPages = listOf(
        IntroPage(
            title = "Bienvenue dans BrightKids!",
            description = "L'apprentissage devient amusant et interactif pour vos enfants",
            emoji = "🌟"
        ),
        IntroPage(
            title = "Apprends les lettres",
            description = "Découvre l'alphabet français et arabe de manière ludique",
            emoji = "📚"
        ),
        IntroPage(
            title = "Trace et dessine",
            description = "Pratique l'écriture en traçant les lettres avec ton doigt",
            emoji = "✏️"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupIndicator()
        setupButton()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = IntroPagerAdapter(introPages)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicator(position)
                updateButton(position)
            }
        })
    }

    private fun setupIndicator() {
        // Indicators will be managed dynamically
        updateIndicator(0)
    }

    private fun updateIndicator(position: Int) {
        // Update indicator dots
        binding.indicator1.alpha = if (position == 0) 1f else 0.3f
        binding.indicator2.alpha = if (position == 1) 1f else 0.3f
        binding.indicator3.alpha = if (position == 2) 1f else 0.3f
    }

    private fun setupButton() {
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < introPages.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                navigateToOnboarding()
            }
        }
        
        binding.btnSkip.setOnClickListener {
            navigateToOnboarding()
        }
        
        updateButton(0)
    }

    private fun updateButton(position: Int) {
        if (position == introPages.size - 1) {
            binding.btnNext.text = "Commencer"
        } else {
            binding.btnNext.text = "Suivant"
        }
    }

    private fun navigateToOnboarding() {
        prefsManager.setIntroCompleted(true)
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    data class IntroPage(
        val title: String,
        val description: String,
        val emoji: String
    )

    class IntroPagerAdapter(private val pages: List<IntroPage>) :
        RecyclerView.Adapter<IntroPagerAdapter.IntroPageViewHolder>() {

        inner class IntroPageViewHolder(private val binding: ItemIntroPageBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(page: IntroPage) {
                binding.tvEmoji.text = page.emoji
                binding.tvTitle.text = page.title
                binding.tvDescription.text = page.description
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroPageViewHolder {
            val binding = ItemIntroPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return IntroPageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: IntroPageViewHolder, position: Int) {
            holder.bind(pages[position])
        }

        override fun getItemCount(): Int = pages.size
    }
}






