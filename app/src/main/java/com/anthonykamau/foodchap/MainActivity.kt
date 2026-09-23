package com.anthonykamau.foodchap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anthonykamau.foodchap.data.local.entity.MealType
import com.anthonykamau.foodchap.data.local.entity.Recipe
import com.anthonykamau.foodchap.databinding.ActivityMainBinding
import com.anthonykamau.foodchap.databinding.ItemRecipeCompactBinding
import com.anthonykamau.foodchap.ui.BottomNavHelper
import com.anthonykamau.foodchap.ui.browse.RecipeAdapter
import com.anthonykamau.foodchap.ui.browse.RecipeListViewModel
import com.anthonykamau.foodchap.ui.detail.RecipeDetailActivity

enum class HomeCategory { BREAKFAST, MAIN_MEAL, SNACK }

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RecipeListViewModel
    private lateinit var adapter: RecipeAdapter

    private var allRecipes: List<Recipe> = emptyList()
    private var currentQuery: String = ""
    private var currentCategory: HomeCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!OnboardingPreference.hasSeenOnboarding(this)) {
            startActivity(android.content.Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomNavHelper.attach(this, binding.bottomNavigation, R.id.nav_recipes)

        val themeIcon = if (ThemePreference.isDarkModeActive(this)) R.drawable.ic_theme_light else R.drawable.ic_theme_dark
        binding.themeToggleButton.setImageResource(themeIcon)
        binding.themeToggleButton.setOnClickListener {
            ThemePreference.toggleTheme(this)
            recreate()
        }

        val isPickMode = intent.getBooleanExtra(EXTRA_PICK_MODE, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(RecipeListViewModel::class.java)

        adapter = RecipeAdapter { recipe ->
            if (isPickMode) {
                setResult(RESULT_OK, android.content.Intent().putExtra(EXTRA_SELECTED_RECIPE_ID, recipe.id))
                finish()
            } else {
                RecipeDetailActivity.start(this, recipe.id)
            }
        }

        binding.recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.recipeRecyclerView.adapter = adapter

        binding.searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s?.toString().orEmpty()
                render()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        binding.filterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            currentCategory = when (checkedIds.firstOrNull()) {
                R.id.chip_breakfast -> HomeCategory.BREAKFAST
                R.id.chip_main_meal -> HomeCategory.MAIN_MEAL
                R.id.chip_snack -> HomeCategory.SNACK
                else -> null
            }
            updateChipColors()
            render()
        }
        updateChipColors()

        viewModel.recipes.observe(this) { recipes ->
            allRecipes = recipes
            render()
        }
    }

    private fun matchesCategory(recipe: Recipe, category: HomeCategory): Boolean {
        return when (category) {
            HomeCategory.BREAKFAST -> recipe.mealType == MealType.BREAKFAST
            HomeCategory.MAIN_MEAL -> recipe.mealType == MealType.LUNCH || recipe.mealType == MealType.SUPPER
            HomeCategory.SNACK -> recipe.mealType == MealType.SNACK
        }
    }

    private fun render() {
        val showingCurated = currentQuery.isBlank() && currentCategory == null
        if (showingCurated) {
            binding.curatedScrollView.visibility = View.VISIBLE
            binding.recipeRecyclerView.visibility = View.GONE
            binding.emptyStateText.visibility = View.GONE
            buildCuratedSections()
        } else {
            binding.curatedScrollView.visibility = View.GONE
            binding.recipeRecyclerView.visibility = View.VISIBLE
            applyFilters()
        }
    }

    private fun buildCuratedSections() {
        binding.curatedContainer.removeAllViews()

        for (category in HomeCategory.entries) {
            val recipesInCategory = allRecipes.filter { matchesCategory(it, category) }.sortedByDescending { it.rating }
            if (recipesInCategory.isEmpty()) continue

            val label = when (category) {
                HomeCategory.BREAKFAST -> "Breakfast"
                HomeCategory.MAIN_MEAL -> "Main Meal"
                HomeCategory.SNACK -> "Snack"
            }

            val header = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
                setPadding(20, 24, 20, 12)
            }
            header.addView(android.widget.TextView(this).apply {
                text = label
                textSize = 17f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.fc_text_primary))
                layoutParams = android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
            header.addView(android.widget.TextView(this).apply {
                text = "See All"
                textSize = 13f
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.fc_accent))
                setOnClickListener { selectChipForCategory(category) }
            })
            binding.curatedContainer.addView(header)

            val horizontalScroll = android.widget.HorizontalScrollView(this).apply {
                isHorizontalScrollBarEnabled = false
            }
            val row = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
                setPadding(20, 0, 20, 0)
            }
            recipesInCategory.take(8).forEach { recipe ->
                val cardBinding = ItemRecipeCompactBinding.inflate(LayoutInflater.from(this), row, false)
                cardBinding.compactRecipeName.text = recipe.name
                cardBinding.compactRecipePrice.text = "KES ${recipe.totalCostKes}"
                val resId = recipe.imageResName?.let { resources.getIdentifier(it, "drawable", packageName) }
                if (resId != null && resId != 0) {
                    cardBinding.compactRecipeImage.setImageResource(resId)
                }
                cardBinding.root.setOnClickListener { RecipeDetailActivity.start(this, recipe.id) }
                row.addView(cardBinding.root)
            }
            horizontalScroll.addView(row)
            binding.curatedContainer.addView(horizontalScroll)
        }
    }

    private fun selectChipForCategory(category: HomeCategory) {
        val chipId = when (category) {
            HomeCategory.BREAKFAST -> R.id.chip_breakfast
            HomeCategory.MAIN_MEAL -> R.id.chip_main_meal
            HomeCategory.SNACK -> R.id.chip_snack
        }
        binding.filterChipGroup.check(chipId)
    }

    private fun updateChipColors() {
        val chips = listOf(binding.chipAll, binding.chipBreakfast, binding.chipMainMeal, binding.chipSnack)
        val accentColor = ContextCompat.getColor(this, R.color.fc_accent)
        val surfaceColor = ContextCompat.getColor(this, R.color.fc_surface)
        val onAccentColor = ContextCompat.getColor(this, R.color.fc_on_accent)
        val textPrimaryColor = ContextCompat.getColor(this, R.color.fc_text_primary)

        chips.forEach { chip ->
            if (chip.isChecked) {
                chip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(accentColor)
                chip.setTextColor(onAccentColor)
            } else {
                chip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(surfaceColor)
                chip.setTextColor(textPrimaryColor)
            }
        }
    }

    private fun applyFilters() {
        val filtered = allRecipes.filter { recipe ->
            val matchesQuery = currentQuery.isBlank() || recipe.name.contains(currentQuery, ignoreCase = true)
            val matchesCategory = currentCategory == null || matchesCategory(recipe, currentCategory!!)
            matchesQuery && matchesCategory
        }
        adapter.submitList(filtered)
        binding.emptyStateText.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_PICK_MODE = "extra_pick_mode"
        const val EXTRA_SELECTED_RECIPE_ID = "extra_selected_recipe_id"

        fun createPickIntent(context: android.content.Context): android.content.Intent {
            return android.content.Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_PICK_MODE, true)
            }
        }
    }
}
