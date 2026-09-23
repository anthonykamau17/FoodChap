package com.anthonykamau.foodchap.ui.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.anthonykamau.foodchap.FoodChapApp
import com.anthonykamau.foodchap.data.local.entity.ShoppingListItem
import com.anthonykamau.foodchap.ui.planner.MealPlannerViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.ceil

class ShoppingListViewModel(app: Application) : AndroidViewModel(app) {

    private val foodChapApp = app as FoodChapApp
    private val mealPlanDao = foodChapApp.database.mealPlanDao()
    private val recipeDao = foodChapApp.database.recipeDao()
    private val shoppingListDao = foodChapApp.database.shoppingListDao()
    private val packageDao = foodChapApp.database.ingredientPackageDao()

    val weekStartDate: String = MealPlannerViewModel.computeThisMondayDateString()

    val items: LiveData<List<ShoppingListItem>> = shoppingListDao.getItemsForWeek(weekStartDate).asLiveData()

    private val _estimatedTotalKes = MutableLiveData<Int>()
    val estimatedTotalKes: LiveData<Int> = _estimatedTotalKes

    fun regenerateFromPlan() {
        viewModelScope.launch {
            val entries = mealPlanDao.getEntriesForWeek(weekStartDate).first()
            val newItems = mutableListOf<ShoppingListItem>()

            for (entry in entries) {
                val lines = recipeDao.getIngredientDetailsForRecipe(entry.recipeId)
                lines.forEach { line ->
                    newItems.add(
                        ShoppingListItem(
                            weekStartDate = weekStartDate,
                            ingredientId = line.ingredientId,
                            ingredientName = line.name,
                            quantity = line.quantity,
                            unit = line.unit,
                            category = line.category,
                            standardizedAmount = line.standardizedAmount,
                            standardizedUnit = line.standardizedUnit,
                            isChecked = false,
                            isManuallyAdded = false
                        )
                    )
                }
            }

            shoppingListDao.clearGeneratedItemsForWeek(weekStartDate)
            shoppingListDao.insertAll(newItems)
            recalculateTotal()
        }
    }

    /** Cheapest-per-unit estimated cost for a single item line, or null if it can't be priced. */
    suspend fun estimateCostForItem(item: ShoppingListItem): Int? {
        val amount = item.standardizedAmount ?: return null
        val unit = item.standardizedUnit ?: return null
        val ingredientId = item.ingredientId ?: return null

        val packages = packageDao.getPackagesForIngredientOnce(ingredientId).filter { it.unit == unit }
        if (packages.isEmpty()) return null

        val cheapestPerUnit = packages.minByOrNull { it.priceKes / it.amount } ?: return null
        val unitsNeeded = ceil(amount / cheapestPerUnit.amount).toInt().coerceAtLeast(1)
        return unitsNeeded * cheapestPerUnit.priceKes
    }

    private fun recalculateTotal() {
        viewModelScope.launch {
            val currentItems = shoppingListDao.getItemsForWeek(weekStartDate).first()
            var total = 0
            for (item in currentItems) {
                total += estimateCostForItem(item) ?: 0
            }
            _estimatedTotalKes.value = total
        }
    }

    fun toggleChecked(item: ShoppingListItem) {
        viewModelScope.launch {
            shoppingListDao.update(item.copy(isChecked = !item.isChecked))
        }
    }
}