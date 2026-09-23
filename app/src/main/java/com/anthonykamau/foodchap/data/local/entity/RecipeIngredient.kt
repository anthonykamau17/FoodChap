package com.anthonykamau.foodchap.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One row per ingredient required by a recipe, with the quantity needed.
 * This table is what powers the "% ingredient match" calculation on
 * the Recipe Results screen: given a set of selected ingredientIds,
 * count how many of a recipe's rows are covered.
 */
@Entity(
    tableName = "recipe_ingredients",
    foreignKeys = [
        ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Ingredient::class, parentColumns = ["id"], childColumns = ["ingredientId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("recipeId"), Index("ingredientId")]
)
data class RecipeIngredient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: Long,
    val ingredientId: Long,
    val quantity: String,           // free text for display, e.g. "3 cups", "a pinch"
    val unit: String?,              // display unit; null when quantity is already descriptive
    val standardizedAmount: Double? = null,  // numeric amount for yield math, e.g. 375.0 — null if not realistically measurable (a pinch, a bunch)
    val standardizedUnit: String? = null     // "g", "ml", or "piece" — must match the ingredient's IngredientPackage unit
)
