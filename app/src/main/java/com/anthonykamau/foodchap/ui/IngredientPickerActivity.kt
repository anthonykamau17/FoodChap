package com.anthonykamau.foodchap.ui.picker

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anthonykamau.foodchap.R
import com.anthonykamau.foodchap.data.local.entity.Ingredient
import com.anthonykamau.foodchap.data.local.entity.IngredientCategory
import com.anthonykamau.foodchap.data.local.entity.IngredientPackage
import com.anthonykamau.foodchap.ui.BottomNavHelper
import com.anthonykamau.foodchap.ui.results.RecipeResultsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class IngredientPickerActivity : AppCompatActivity() {

    private lateinit var findButton: Button
    private lateinit var listContainer: LinearLayout

    private var allIngredients: List<Ingredient> = emptyList()
    private var packagesByIngredientId: Map<Long, List<IngredientPackage>> = emptyMap()
    private var currentQuery: String = ""

    private var accentColor = 0
    private var surfaceColor = 0
    private var onAccentColor = 0
    private var textPrimaryColor = 0
    private var backgroundColor = 0
    private var textSecondaryColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accentColor = ContextCompat.getColor(this, R.color.fc_accent)
        surfaceColor = ContextCompat.getColor(this, R.color.fc_surface)
        onAccentColor = ContextCompat.getColor(this, R.color.fc_on_accent)
        textPrimaryColor = ContextCompat.getColor(this, R.color.fc_text_primary)
        backgroundColor = ContextCompat.getColor(this, R.color.fc_background)
        textSecondaryColor = ContextCompat.getColor(this, R.color.fc_text_secondary)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val titleRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(40, 28, 40, 4)
        }
        titleRow.addView(TextView(this).apply {
            text = "What ingredients do you have?"
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(textPrimaryColor)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        })
        titleRow.addView(TextView(this).apply {
            text = "Clear"
            textSize = 13f
            setTextColor(accentColor)
            setOnClickListener {
                IngredientSelectionStore.clear()
                renderList()
                updateButtonText()
            }
        })
        rootLayout.addView(titleRow)

        val subtitle = TextView(this).apply {
            text = "Tap a priced ingredient to pick the size you have. We'll estimate how much you can cook."
            textSize = 13f
            setTextColor(textSecondaryColor)
            setPadding(40, 0, 40, 16)
        }
        rootLayout.addView(subtitle)

        val searchBox = EditText(this).apply {
            hint = "Search ingredients..."
            setHintTextColor(textSecondaryColor)
            setTextColor(textPrimaryColor)
            background = ContextCompat.getDrawable(this@IngredientPickerActivity, R.drawable.bg_search_bar)
            setPadding(32, 24, 32, 24)
        }
        rootLayout.addView(searchBox, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(40, 0, 40, 20) })

        val scrollView = ScrollView(this)
        listContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 0, 40, 20)
        }
        scrollView.addView(listContainer)
        rootLayout.addView(scrollView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        findButton = Button(this).apply {
            text = "Find Recipes"
            setTextColor(onAccentColor)
            background = ContextCompat.getDrawable(this@IngredientPickerActivity, R.drawable.bg_button_primary)
            setOnClickListener { onFindRecipesClicked() }
        }
        rootLayout.addView(findButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(40, 20, 40, 20) })

        val bottomNav = BottomNavigationView(this)
        rootLayout.addView(bottomNav, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        val decoratedFrame = android.widget.FrameLayout(this).apply {
            setBackgroundColor(backgroundColor)
        }
        val blobTopRight = android.widget.ImageView(this).apply {
            setImageResource(R.drawable.bg_blob)
            alpha = 0.15f
        }
        decoratedFrame.addView(blobTopRight, android.widget.FrameLayout.LayoutParams(560, 560).apply {
            gravity = android.view.Gravity.TOP or android.view.Gravity.END
            topMargin = -140
            rightMargin = -140
        })
        val blobBottomLeft = android.widget.ImageView(this).apply {
            setImageResource(R.drawable.bg_blob)
            alpha = 0.12f
        }
        decoratedFrame.addView(blobBottomLeft, android.widget.FrameLayout.LayoutParams(500, 500).apply {
            gravity = android.view.Gravity.BOTTOM or android.view.Gravity.START
            bottomMargin = -100
            leftMargin = -100
        })
        decoratedFrame.addView(rootLayout)
        setContentView(decoratedFrame)

        BottomNavHelper.attach(this, bottomNav, R.id.nav_cook)

        searchBox.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s?.toString().orEmpty()
                renderList()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        val viewModel = ViewModelProvider(this)[IngredientPickerViewModel::class.java]
        viewModel.ingredients.observe(this) { ingredients ->
            allIngredients = ingredients
            renderList()
        }
        viewModel.packages.observe(this) { packages ->
            packagesByIngredientId = packages.groupBy { it.ingredientId }
            renderList()
        }
        updateButtonText()
    }

    private fun renderList() {
        listContainer.removeAllViews()

        val filtered = if (currentQuery.isBlank()) {
            allIngredients
        } else {
            allIngredients.filter { it.name.contains(currentQuery, ignoreCase = true) }
        }

        val grouped = filtered.groupBy { it.category }
        for (category in IngredientCategory.entries) {
            val items = grouped[category] ?: continue
            addCategoryHeader(listContainer, category.name)
            addChipGroup(listContainer, items)
        }

        if (filtered.isEmpty()) {
            listContainer.addView(TextView(this).apply {
                text = "No ingredients match \"$currentQuery\""
                textSize = 14f
                setTextColor(textSecondaryColor)
                setPadding(0, 20, 0, 20)
            })
        }
    }

    private fun addCategoryHeader(container: LinearLayout, name: String) {
        container.addView(TextView(this).apply {
            text = name.lowercase().replaceFirstChar { it.uppercase() }
            textSize = 15f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(accentColor)
            setPadding(0, 32, 0, 10)
        })
    }

    private fun addChipGroup(container: LinearLayout, items: List<Ingredient>) {
        val chipGroup = ChipGroup(this).apply {
            isSingleSelection = false
        }
        items.forEach { ingredient ->
            val packagesForThis = packagesByIngredientId[ingredient.id].orEmpty()
            val hasPackages = packagesForThis.isNotEmpty()
            val isSelected = ingredient.id in IngredientSelectionStore.selectedIds

            val chip = Chip(this).apply {
                text = if (hasPackages && isSelected) {
                    "${ingredient.name} (${IngredientSelectionStore.selectedPackages[ingredient.id]?.sizeLabel})"
                } else {
                    ingredient.name
                }
                isCheckable = !hasPackages
                isChecked = !hasPackages && isSelected
                applyChipColors(this, isSelected)

                if (hasPackages) {
                    setOnClickListener {
                        showPackageDialog(ingredient, packagesForThis)
                    }
                } else {
                    setOnCheckedChangeListener { chipView, checked ->
                        if (checked) IngredientSelectionStore.selectedIds.add(ingredient.id) else IngredientSelectionStore.selectedIds.remove(ingredient.id)
                        applyChipColors(chipView as Chip, checked)
                        updateButtonText()
                    }
                }
            }
            chipGroup.addView(chip)
        }
        container.addView(chipGroup)
    }

    private fun showPackageDialog(ingredient: Ingredient, packages: List<IngredientPackage>) {
        val labels = packages.map { "${it.sizeLabel} — KES ${it.priceKes}" }.toMutableList()
        labels.add("I don't have this")

        val currentPackageId = IngredientSelectionStore.selectedPackages[ingredient.id]?.id
        val currentIndex = if (ingredient.id !in IngredientSelectionStore.selectedIds) {
            labels.size - 1
        } else {
            packages.indexOfFirst { it.id == currentPackageId }
        }

        AlertDialog.Builder(this, R.style.FoodChapAlertDialog)
            .setTitle(ingredient.name)
            .setSingleChoiceItems(labels.toTypedArray(), currentIndex) { dialog, which ->
                if (which == labels.size - 1) {
                    IngredientSelectionStore.selectedIds.remove(ingredient.id)
                    IngredientSelectionStore.selectedPackages.remove(ingredient.id)
                } else {
                    IngredientSelectionStore.selectedIds.add(ingredient.id)
                    IngredientSelectionStore.selectedPackages[ingredient.id] = packages[which]
                }
                updateButtonText()
                dialog.dismiss()
                renderList()
            }
            .show()
    }

    private fun applyChipColors(chip: Chip, isSelected: Boolean) {
        if (isSelected) {
            chip.chipBackgroundColor = ColorStateList.valueOf(accentColor)
            chip.setTextColor(onAccentColor)
        } else {
            chip.chipBackgroundColor = ColorStateList.valueOf(surfaceColor)
            chip.setTextColor(textPrimaryColor)
        }
    }

    private fun updateButtonText() {
        val count = IngredientSelectionStore.selectedIds.size
        findButton.text = if (count == 0) "Find Recipes" else "Find Recipes ($count selected)"
    }

    private fun onFindRecipesClicked() {
        if (IngredientSelectionStore.selectedIds.isEmpty()) {
            Toast.makeText(this, "Select at least one ingredient", Toast.LENGTH_SHORT).show()
            return
        }
        val packageIngredientIds = IngredientSelectionStore.selectedPackages.keys.toLongArray()
        val packageIds = IngredientSelectionStore.selectedPackages.values.map { it.id }.toLongArray()
        RecipeResultsActivity.start(this, IngredientSelectionStore.selectedIds.toLongArray(), packageIngredientIds, packageIds)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, IngredientPickerActivity::class.java))
        }
    }
}