package com.anthonykamau.foodchap.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One purchasable size of an ingredient, e.g. "Milk — 500ml — KES 60".
 * An ingredient can have several packages (500ml / 1L / 2L milk),
 * each selectable independently in the Ingredient Picker.
 */
@Entity(
    tableName = "ingredient_packages",
    foreignKeys = [
        ForeignKey(entity = Ingredient::class, parentColumns = ["id"], childColumns = ["ingredientId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("ingredientId")]
)
data class IngredientPackage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ingredientId: Long,
    val sizeLabel: String,      // e.g. "500ml", "1kg", "1 piece"
    val amount: Double,         // numeric quantity, e.g. 500
    val unit: String,           // "ml", "g", "piece" — must match RecipeIngredient's standardizedUnit for this ingredient
    val priceKes: Int
)