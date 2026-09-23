package com.anthonykamau.foodchap.data.local

import androidx.room.TypeConverter
import com.anthonykamau.foodchap.data.local.entity.Difficulty
import com.anthonykamau.foodchap.data.local.entity.IngredientCategory
import com.anthonykamau.foodchap.data.local.entity.MealType

/**
 * Room stores enums as their name() string. Without these converters,
 * the project will not compile — Room has no built-in enum support.
 */
class Converters {

    @TypeConverter
    fun fromMealType(value: MealType): String = value.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)

    @TypeConverter
    fun fromDifficulty(value: Difficulty): String = value.name

    @TypeConverter
    fun toDifficulty(value: String): Difficulty = Difficulty.valueOf(value)

    @TypeConverter
    fun fromIngredientCategory(value: IngredientCategory): String = value.name

    @TypeConverter
    fun toIngredientCategory(value: String): IngredientCategory = IngredientCategory.valueOf(value)
}
