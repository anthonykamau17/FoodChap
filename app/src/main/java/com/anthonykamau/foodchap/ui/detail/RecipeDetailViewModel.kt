package com.anthonykamau.foodchap.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anthonykamau.foodchap.FoodChapApp
import com.anthonykamau.foodchap.data.local.dao.IngredientLine
import com.anthonykamau.foodchap.data.local.entity.Recipe
import com.anthonykamau.foodchap.data.local.entity.RecipeStep
import kotlinx.coroutines.launch

class RecipeDetailViewModel(app: Application, private val recipeId: Long) : AndroidViewModel(app) {

    private val recipeDao = (app as FoodChapApp).database.recipeDao()

    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe?> = _recipe

    private val _ingredients = MutableLiveData<List<IngredientLine>>()
    val ingredients: LiveData<List<IngredientLine>> = _ingredients

    private val _steps = MutableLiveData<List<RecipeStep>>()
    val steps: LiveData<List<RecipeStep>> = _steps

    private val _pairings = MutableLiveData<List<Recipe>>()
    val pairings: LiveData<List<Recipe>> = _pairings


    init {
        viewModelScope.launch {
            _recipe.value = recipeDao.getRecipeById(recipeId)
            _ingredients.value = recipeDao.getIngredientDetailsForRecipe(recipeId)
            _steps.value = recipeDao.getStepsForRecipe(recipeId)
            _pairings.value = recipeDao.getPairingsForRecipe(recipeId)
        }
    }

    class Factory(private val app: Application, private val recipeId: Long) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecipeDetailViewModel(app, recipeId) as T
        }
    }
}