package com.example.brightkids

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.brightkids.learning.databinding.ActivityOnboardingBinding
import com.brightkids.learning.databinding.ItemOnboardingAvatarBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private var selectedAvatarPosition = 0
    private lateinit var avatarAdapter: AvatarAdapter
    private val prefsManager by lazy { PrefsManager.getInstance(this) }
    private val avatarEmojis = listOf(
        "🦁", "🐯", "🐻", "🐰",
        "🦊", "🐼", "🐨", "🐸",
        "🦄", "🐝", "🦕", "🐙"
    )
    private var currentPage = 0
    private val totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupIndicators()
        setupNavigation()
        updateStepNumber()
    }

    private fun setupViewPager() {
        val adapter = OnboardingPagerAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false // Disable swipe
        
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                updateStepNumber()
                updateNavigationButtons()
                updateIndicators(position)
            }
        })
    }

    private fun setupIndicators() {
        binding.pageIndicators.removeAllViews()
        for (i in 0 until totalPages) {
            val indicator = ImageView(this)
            val size = resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 4
            val params = ViewGroup.MarginLayoutParams(size, size)
            params.setMargins(8, 0, 8, 0)
            indicator.layoutParams = params
            indicator.setImageResource(
                if (i == 0) com.brightkids.learning.R.drawable.indicator_dot_active
                else com.brightkids.learning.R.drawable.indicator_dot_inactive
            )
            binding.pageIndicators.addView(indicator)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until binding.pageIndicators.childCount) {
            val indicator = binding.pageIndicators.getChildAt(i) as ImageView
            indicator.setImageResource(
                if (i == position) com.brightkids.learning.R.drawable.indicator_dot_active
                else com.brightkids.learning.R.drawable.indicator_dot_inactive
            )
        }
    }

    private fun setupNavigation() {
        binding.btnNext.setOnClickListener {
            if (canProceed()) {
                if (currentPage < totalPages - 1) {
                    binding.viewPager.currentItem = currentPage + 1
                } else {
                    completeOnboarding()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            if (currentPage > 0) {
                binding.viewPager.currentItem = currentPage - 1
            }
        }
        
        updateNavigationButtons()
    }

    private fun canProceed(): Boolean {
        // Name input removed: allow proceeding (avatar is always selectable; default is 0).
        return true
    }

    private fun completeOnboarding() {
        // Save user profile
        prefsManager.setUserName("")
        prefsManager.setUserAvatar(selectedAvatarPosition)
        prefsManager.setOnboardingCompleted(true)

        // Navigate to MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun updateStepNumber() {
        binding.tvStepNumber.text = "${currentPage + 1}"
    }

    private fun updateNavigationButtons() {
        // Show/hide back button
        binding.btnBack.visibility = if (currentPage > 0) View.VISIBLE else View.GONE
        
        // Update button text and style
        if (currentPage == totalPages - 1) {
            binding.btnNext.text = "COMPLETE"
            binding.btnNext.setBackgroundResource(com.brightkids.learning.R.drawable.gradient_button_green)
        } else {
            binding.btnNext.text = "NEXT"
            binding.btnNext.setBackgroundResource(com.brightkids.learning.R.drawable.gradient_button_blue_teal)
        }
    }

    inner class OnboardingPagerAdapter : RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingPageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingPageViewHolder {
            val binding = ItemOnboardingAvatarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return OnboardingPageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: OnboardingPageViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount(): Int = totalPages

        inner class OnboardingPageViewHolder(
            private val binding: ItemOnboardingAvatarBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind() {
                setupAvatarGrid()
            }

            private fun setupAvatarGrid() {
                avatarAdapter = AvatarAdapter(avatarEmojis, selectedAvatarPosition) { position ->
                    selectedAvatarPosition = position
                    avatarAdapter.notifyDataSetChanged()
                }
                binding.recyclerViewAvatars.layoutManager = GridLayoutManager(this@OnboardingActivity, 4)
                binding.recyclerViewAvatars.adapter = avatarAdapter
            }

        }
    }
}
