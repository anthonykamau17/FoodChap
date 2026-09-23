package com.anthonykamau.foodchap.ui.picker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.anthonykamau.foodchap.FoodChapApp
import com.anthonykamau.foodchap.data.local.entity.Ingredient
import com.anthonykamau.foodchap.data.local.entity.IngredientPackage

class IngredientPickerViewModel(app: Application) : AndroidViewModel(app) {
    private val foodChapApp = app as FoodChapApp
    private val ingredientDao = foodChapApp.database.ingredientDao()
    private val packageDao = foodChapApp.database.ingredientPackageDao()

    val ingredients: LiveData<List<Ingredient>> = ingredientDao.getAllIngredients().asLiveData()
    val packages: LiveData<List<IngredientPackage>> = packageDao.getAllPackages().asLiveData()
}