package com.anthonykamau.foodchap

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class OnboardingActivity : AppCompatActivity() {

    private data class Slide(val emoji: String, val title: String, val description: String)

    private val slides = listOf(
        Slide("🍲", "Welcome to FoodChap", "Browse 100+ Kenyan and global recipes, with real ingredients and step-by-step instructions."),
        Slide("🥕", "What Can I Cook?", "Select the ingredients you have — priced at real Kenyan market rates — and we'll show matching recipes with an estimate of how much you can make."),
        Slide("📅", "Meal Planner", "Plan your whole week. Tap \"+ Add to Meal Planner\" on any recipe, or assign one directly from the Planner tab."),
        Slide("🛒", "Shopping List", "Your plan turns into a shopping list automatically, grouped by category with a running KES total.")
    )

    private var currentIndex = 0

    private lateinit var emojiText: TextView
    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var dotsContainer: LinearLayout
    private lateinit var nextButton: Button

    private var accentColor = 0
    private var surfaceColor = 0
    private var textPrimaryColor = 0
    private var textSecondaryColor = 0
    private var backgroundColor = 0
    private var onAccentColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accentColor = ContextCompat.getColor(this, R.color.fc_accent)
        surfaceColor = ContextCompat.getColor(this, R.color.fc_surface)
        textPrimaryColor = ContextCompat.getColor(this, R.color.fc_text_primary)
        textSecondaryColor = ContextCompat.getColor(this, R.color.fc_text_secondary)
        backgroundColor = ContextCompat.getColor(this, R.color.fc_background)
        onAccentColor = ContextCompat.getColor(this, R.color.fc_on_accent)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(backgroundColor)
            setPadding(48, 48, 48, 48)
        }

        val skipButton = TextView(this).apply {
            text = "Skip"
            textSize = 14f
            setTextColor(textSecondaryColor)
            gravity = android.view.Gravity.END
            setOnClickListener { finishOnboarding() }
        }
        rootLayout.addView(skipButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        val centerSpace = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
        }

        emojiText = TextView(this).apply {
            textSize = 72f
            gravity = android.view.Gravity.CENTER
        }
        centerSpace.addView(emojiText, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 0, 0, 32) })

        titleText = TextView(this).apply {
            textSize = 24f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(textPrimaryColor)
            gravity = android.view.Gravity.CENTER
        }
        centerSpace.addView(titleText, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 0, 0, 16) })

        descriptionText = TextView(this).apply {
            textSize = 15f
            setTextColor(textSecondaryColor)
            gravity = android.view.Gravity.CENTER
        }
        centerSpace.addView(descriptionText, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        rootLayout.addView(centerSpace, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        dotsContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER
        }
        rootLayout.addView(dotsContainer, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 0, 0, 32) })

        nextButton = Button(this).apply {
            setTextColor(onAccentColor)
            background = ContextCompat.getDrawable(this@OnboardingActivity, R.drawable.bg_button_primary)
            setOnClickListener { onNextClicked() }
        }
        rootLayout.addView(nextButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        setContentView(rootLayout)
        renderSlide()
    }

    private fun renderSlide() {
        val slide = slides[currentIndex]
        emojiText.text = slide.emoji
        titleText.text = slide.title
        descriptionText.text = slide.description
        nextButton.text = if (currentIndex == slides.size - 1) "Get Started" else "Next"

        dotsContainer.removeAllViews()
        slides.indices.forEach { i ->
            val dot = android.widget.ImageView(this).apply {
                setImageDrawable(ContextCompat.getDrawable(this@OnboardingActivity, R.drawable.bg_price_pill))
                alpha = if (i == currentIndex) 1f else 0.3f
            }
            dotsContainer.addView(dot, LinearLayout.LayoutParams(20, 8).apply {
                setMargins(4, 0, 4, 0)
            })
        }
    }

    private fun onNextClicked() {
        if (currentIndex < slides.size - 1) {
            currentIndex++
            renderSlide()
        } else {
            finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        OnboardingPreference.markOnboardingSeen(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}