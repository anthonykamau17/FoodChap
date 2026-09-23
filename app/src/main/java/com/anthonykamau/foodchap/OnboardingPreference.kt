package com.anthonykamau.foodchap

import android.content.Context

object OnboardingPreference {
    private const val PREFS_NAME = "foodchap_prefs"
    private const val KEY_SEEN_ONBOARDING = "seen_onboarding"

    fun hasSeenOnboarding(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SEEN_ONBOARDING, false)
    }

    fun markOnboardingSeen(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SEEN_ONBOARDING, true).apply()
    }
}