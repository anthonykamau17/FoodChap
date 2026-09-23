package com.anthonykamau.foodchap.ui.results

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anthonykamau.foodchap.FoodChapApp
import com.anthonykamau.foodchap.data.local.dao.RecipeMatch
import com.anthonykamau.foodchap.data.local.entity.IngredientPackage
import kotlinx.coroutines.launch
import kotlin.math.floor

data class RecipeResultRow(val match: RecipeMatch, val yieldBatches: Int?)

class RecipeResultsViewModel(
    app: Application,
    private val selectedIngredientIds: List<Long>,
    private val selectedPackageMap: Map<Long, Long> // ingredientId -> packageId
) : AndroidViewModel(app) {

    private val foodChapApp = app as FoodChapApp
    private val recipeDao = foodChapApp.database.recipeDao()
    private val packageDao = foodChapApp.database.ingredientPackageDao()

    private val _results = MutableLiveData<List<RecipeResultRow>>()
    val results: LiveData<List<RecipeResultRow>> = _results

    init {
        viewModelScope.launch {
            val rawMatches = recipeDao.getMatchesForIngredients(selectedIngredientIds)
                .filter { it.matchPercent >= 50 }
                .sortedByDescending { it.matchPercent }

            val packagesById = mutableMapOf<Long, IngredientPackage>()
            for (pkgId in selectedPackageMap.values.toSet()) {
                packageDao.getPackageById(pkgId)?.let { packagesById[pkgId] = it }
            }

            val rows = rawMatches.map { match ->
                val requiredRows = recipeDao.getIngredientsForRecipe(match.recipe.id)
                    .filter { it.standardizedAmount != null && it.standardizedUnit != null }

                val yieldBatches: Int? = if (requiredRows.isEmpty()) {
                    null
                } else {
                    var minBatches = Double.MAX_VALUE
                    var allCovered = true
                    for (row in requiredRows) {
                        val pkgId = selectedPackageMap[row.ingredientId]
                        val pkg = pkgId?.let { packagesById[it] }
                        if (pkg == null || pkg.unit != row.standardizedUnit) {
                            allCovered = false
                            break
                        }
                        val batches = pkg.amount / row.standardizedAmount!!
                        if (batches < minBatches) minBatches = batches
                    }
                    if (allCovered) floor(minBatches).toInt().coerceAtLeast(0) else null
                }

                RecipeResultRow(match, yieldBatches)
            }
            _results.value = rows
        }
    }

    class Factory(
        private val app: Application,
        private val selectedIngredientIds: List<Long>,
        private val selectedPackageMap: Map<Long, Long>
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeResultsViewModel(app, selectedIngredientIds, selectedPackageMap) as T
        }
    }
}