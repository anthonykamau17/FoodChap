package com.anthonykamau.foodchap.ui.browse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.anthonykamau.foodchap.FoodChapApp
import com.anthonykamau.foodchap.data.local.entity.Recipe

class RecipeListViewModel(app: Application) : AndroidViewModel(app) {

    private val recipeDao = (app as FoodChapApp).database.recipeDao()

    val recipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes().asLiveData()
}