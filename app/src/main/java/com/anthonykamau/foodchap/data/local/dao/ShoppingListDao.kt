package com.anthonykamau.foodchap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anthonykamau.foodchap.data.local.entity.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ShoppingListItem>)

    @Update
    suspend fun update(item: ShoppingListItem)

    @Query("SELECT * FROM shopping_list_items WHERE weekStartDate = :weekStartDate ORDER BY category ASC, ingredientName ASC")
    fun getItemsForWeek(weekStartDate: String): Flow<List<ShoppingListItem>>

    @Query("DELETE FROM shopping_list_items WHERE weekStartDate = :weekStartDate AND isManuallyAdded = 0")
    suspend fun clearGeneratedItemsForWeek(weekStartDate: String)
}
