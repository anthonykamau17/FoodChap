package com.anthonykamau.foodchap.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anthonykamau.foodchap.data.local.entity.MealPlanEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: MealPlanEntry): Long

    @Delete
    suspend fun delete(entry: MealPlanEntry)

    @Query("SELECT * FROM meal_plan_entries WHERE weekStartDate = :weekStartDate")
    fun getEntriesForWeek(weekStartDate: String): Flow<List<MealPlanEntry>>

    @Query("""
        SELECT * FROM meal_plan_entries
        WHERE weekStartDate = :weekStartDate AND dayOfWeek = :dayOfWeek AND mealType = :mealType
        LIMIT 1
    """)
    suspend fun getEntryForSlot(weekStartDate: String, dayOfWeek: Int, mealType: String): MealPlanEntry?
}
