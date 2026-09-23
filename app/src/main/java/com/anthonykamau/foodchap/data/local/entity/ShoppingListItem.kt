package com.anthonykamau.foodchap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single checkable line on the shopping list. Generated from a
 * week's MealPlanEntries (quantities for repeated ingredients merged),
 * but stored as its own table so the user can check items off, add
 * their own extra items, and the list survives independently of the
 * plan it was generated from.
 */
@Entity(tableName = "shopping_list_items")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weekStartDate: String,        // ties this item to the week it was generated for
    val ingredientId: Long? = null,   // null for manually-added items with no matching catalog ingredient
    val ingredientName: String,
    val quantity: String,
    val unit: String?,
    val category: IngredientCategory,
    val standardizedAmount: Double? = null,
    val standardizedUnit: String? = null,
    val isChecked: Boolean = false,
    val isManuallyAdded: Boolean = false   // true for items the user typed in themselves
)
