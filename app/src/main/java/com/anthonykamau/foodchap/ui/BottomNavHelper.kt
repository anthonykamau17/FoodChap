package com.anthonykamau.foodchap.ui

import android.content.Intent
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import com.anthonykamau.foodchap.MainActivity
import com.anthonykamau.foodchap.R
import com.anthonykamau.foodchap.ui.picker.IngredientPickerActivity
import com.anthonykamau.foodchap.ui.planner.MealPlannerActivity
import com.anthonykamau.foodchap.ui.shopping.ShoppingListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Wires up a BottomNavigationView the same way on every screen: inflate the
 * shared menu, highlight the current tab, and navigate to the right Activity
 * when a different tab is tapped.
 */
object BottomNavHelper {

    fun attach(activity: AppCompatActivity, bottomNav: BottomNavigationView, selectedId: Int) {
        MenuInflater(activity).inflate(R.menu.bottom_nav_menu, bottomNav.menu)
        bottomNav.selectedItemId = selectedId

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == selectedId) {
                true
            } else {
                val target: Class<*> = when (item.itemId) {
                    R.id.nav_recipes -> MainActivity::class.java
                    R.id.nav_cook -> IngredientPickerActivity::class.java
                    R.id.nav_planner -> MealPlannerActivity::class.java
                    R.id.nav_shopping -> ShoppingListActivity::class.java
                    else -> return@setOnItemSelectedListener false
                }
                val intent = Intent(activity, target)
                if (target == MainActivity::class.java) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                activity.startActivity(intent)
                activity.finish()
                true
            }
        }
    }
}