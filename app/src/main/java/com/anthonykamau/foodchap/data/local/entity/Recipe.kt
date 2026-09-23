package com.anthonykamau.foodchap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MealType { BREAKFAST, LUNCH, SUPPER, SNACK }
enum class Difficulty { EASY, MEDIUM, HARD }

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mealType: MealType,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val difficulty: Difficulty,
    val rating: Float,              // 0.0 - 5.0
    val totalCostKes: Int,          // total ingredient cost for the recipe, in KES
    val servings: Int,
    val imageResName: String?,      // web URL to the recipe photo; null until one is sourced        // local drawable name or file path; null until photo is sourced
    val description: String
)
