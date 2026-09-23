package com.anthonykamau.foodchap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anthonykamau.foodchap.data.local.entity.IngredientPackage
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientPackageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(packages: List<IngredientPackage>)

    @Query("SELECT * FROM ingredient_packages WHERE ingredientId = :ingredientId")
    fun getPackagesForIngredient(ingredientId: Long): Flow<List<IngredientPackage>>

    @Query("SELECT * FROM ingredient_packages WHERE ingredientId = :ingredientId")
    suspend fun getPackagesForIngredientOnce(ingredientId: Long): List<IngredientPackage>

    @Query("SELECT * FROM ingredient_packages")
    fun getAllPackages(): Flow<List<IngredientPackage>>

    @Query("SELECT * FROM ingredient_packages WHERE id = :packageId")
    suspend fun getPackageById(packageId: Long): IngredientPackage?
}