package com.anthonykamau.foodchap.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anthonykamau.foodchap.data.local.dao.IngredientDao
import com.anthonykamau.foodchap.data.local.dao.IngredientPackageDao
import com.anthonykamau.foodchap.data.local.dao.MealPlanDao
import com.anthonykamau.foodchap.data.local.dao.RecipeDao
import com.anthonykamau.foodchap.data.local.dao.ShoppingListDao
import com.anthonykamau.foodchap.data.local.entity.Ingredient
import com.anthonykamau.foodchap.data.local.entity.IngredientPackage
import com.anthonykamau.foodchap.data.local.entity.RecipePairing
import com.anthonykamau.foodchap.data.local.entity.MealPlanEntry
import com.anthonykamau.foodchap.data.local.entity.Recipe
import com.anthonykamau.foodchap.data.local.entity.RecipeIngredient
import com.anthonykamau.foodchap.data.local.entity.RecipeStep
import com.anthonykamau.foodchap.data.local.entity.ShoppingListItem

@Database(
    entities = [
        Recipe::class,
        Ingredient::class,
        RecipeIngredient::class,
        RecipeStep::class,
        MealPlanEntry::class,
        ShoppingListItem::class,
        IngredientPackage::class,
        RecipePairing::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun ingredientPackageDao(): IngredientPackageDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "foodchap.db"
                )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}
