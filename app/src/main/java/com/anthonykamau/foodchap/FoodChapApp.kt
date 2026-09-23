package com.anthonykamau.foodchap

import android.app.Application
import com.anthonykamau.foodchap.data.local.AppDatabase
import com.anthonykamau.foodchap.data.seed.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FoodChapApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        ThemePreference.applySavedTheme(this)
        database = AppDatabase.getInstance(this)
        applicationScope.launch {
            SeedData.populateIfEmpty(database)
        }
    }
}