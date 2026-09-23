package com.anthonykamau.foodchap.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One assignment of a recipe to a day + meal slot on the weekly planner.
 * weekStartDate anchors this entry to a specific week (stored as
 * "yyyy-MM-dd" for the Monday of that week) so old weeks can be kept
 * or cleared independently, and the Shopping List can be generated
 * from "this week's entries" specifically.
 */
@Entity(
    tableName = "meal_plan_entries",
    foreignKeys = [
        ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("recipeId"), Index("weekStartDate")]
)
data class MealPlanEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weekStartDate: String,   // "yyyy-MM-dd", Monday of the relevant week
    val dayOfWeek: Int,          // 1 = Monday ... 7 = Sunday
    val mealType: MealType,      // BREAKFAST, LUNCH, SUPPER (SNACK unused here but allowed)
    val recipeId: Long
)
