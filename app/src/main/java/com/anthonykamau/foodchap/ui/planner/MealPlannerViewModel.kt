package com.anthonykamau.foodchap.ui.planner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.anthonykamau.foodchap.FoodChapApp
import com.anthonykamau.foodchap.data.local.entity.MealPlanEntry
import com.anthonykamau.foodchap.data.local.entity.MealType
import com.anthonykamau.foodchap.data.local.entity.Recipe
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MealPlannerViewModel(app: Application) : AndroidViewModel(app) {

    private val foodChapApp = app as FoodChapApp
    private val mealPlanDao = foodChapApp.database.mealPlanDao()
    private val recipeDao = foodChapApp.database.recipeDao()

    val weekStartDate: String = computeThisMondayDateString()

    val entries: LiveData<List<MealPlanEntry>> = mealPlanDao.getEntriesForWeek(weekStartDate).asLiveData()

    private val _recipesById = MutableLiveData<Map<Long, Recipe>>()
    val recipesById: LiveData<Map<Long, Recipe>> = _recipesById

    init {
        viewModelScope.launch {
            _recipesById.value = recipeDao.getAllRecipesOnce().associateBy { it.id }
        }
    }

    fun assignRecipe(dayOfWeek: Int, mealType: MealType, recipeId: Long) {
        viewModelScope.launch {
            val existing = mealPlanDao.getEntryForSlot(weekStartDate, dayOfWeek, mealType.name)
            if (existing != null) {
                mealPlanDao.upsert(existing.copy(recipeId = recipeId))
            } else {
                mealPlanDao.upsert(
                    MealPlanEntry(weekStartDate = weekStartDate, dayOfWeek = dayOfWeek, mealType = mealType, recipeId = recipeId)
                )
            }
        }
    }

    fun clearSlot(entry: MealPlanEntry) {
        viewModelScope.launch {
            mealPlanDao.delete(entry)
        }
    }

    companion object {
        fun computeThisMondayDateString(): String {
            val cal = Calendar.getInstance()
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            val diff = if (dayOfWeek == Calendar.SUNDAY) -6 else Calendar.MONDAY - dayOfWeek
            cal.add(Calendar.DAY_OF_MONTH, diff)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(cal.time)
        }
    }
}