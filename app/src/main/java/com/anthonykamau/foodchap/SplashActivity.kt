package com.anthonykamau.foodchap

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backgroundColor = ContextCompat.getColor(this, R.color.fc_background)
        val textPrimaryColor = ContextCompat.getColor(this, R.color.fc_text_primary)
        val textSecondaryColor = ContextCompat.getColor(this, R.color.fc_text_secondary)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setBackgroundColor(backgroundColor)
        }

        val icon = ImageView(this).apply {
            setImageResource(R.mipmap.ic_launcher)
        }
        rootLayout.addView(icon, LinearLayout.LayoutParams(160, 160).apply {
            bottomMargin = 24
        })

        rootLayout.addView(TextView(this).apply {
            text = "FoodChap"
            textSize = 28f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(textPrimaryColor)
            gravity = android.view.Gravity.CENTER
        })

        rootLayout.addView(TextView(this).apply {
            text = "Kenyan recipes, planned simply"
            textSize = 13f
            setTextColor(textSecondaryColor)
            gravity = android.view.Gravity.CENTER
            setPadding(0, 8, 0, 0)
        })

        setContentView(rootLayout)

        Handler(Looper.getMainLooper()).postDelayed({
            val nextActivity = if (OnboardingPreference.hasSeenOnboarding(this)) {
                MainActivity::class.java
            } else {
                OnboardingActivity::class.java
            }
            startActivity(Intent(this, nextActivity))
            finish()
        }, 1500)
    }
}