package com.anthonykamau.foodchap.ui.picker

import com.anthonykamau.foodchap.data.local.entity.IngredientPackage

/**
 * Holds ingredient selections in memory so they survive navigating away
 * from and back to Cook (bottom nav, screen recreation, etc). Resets
 * only when the app process is killed, or the user taps Clear.
 */
object IngredientSelectionStore {
    val selectedIds = mutableSetOf<Long>()
    val selectedPackages = mutableMapOf<Long, IngredientPackage>()

    fun clear() {
        selectedIds.clear()
        selectedPackages.clear()
    }
}