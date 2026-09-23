package com.anthonykamau.foodchap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class IngredientCategory { STAPLE, VEGETABLE, PROTEIN, SPICE, DAIRY, OTHER }

/**
 * Master catalog of selectable ingredients — powers the multi-select
 * Ingredient Picker screen. Distinct from RecipeIngredient, which links
 * a specific quantity of an Ingredient to a specific Recipe.
 */
@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: IngredientCategory
)
