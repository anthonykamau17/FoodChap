package com.anthonykamau.foodchap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anthonykamau.foodchap.data.local.entity.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredients: List<Ingredient>): List<Long>

    @Query("SELECT * FROM ingredients ORDER BY category ASC, name ASC")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredients WHERE category = :category ORDER BY name ASC")
    fun getIngredientsByCategory(category: String): Flow<List<Ingredient>>
}
