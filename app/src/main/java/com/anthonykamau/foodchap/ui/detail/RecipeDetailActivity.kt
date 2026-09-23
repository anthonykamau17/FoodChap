package com.anthonykamau.foodchap.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anthonykamau.foodchap.R
import com.anthonykamau.foodchap.ui.BottomNavHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.anthonykamau.foodchap.FoodChapApp
import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.anthonykamau.foodchap.data.local.dao.IngredientLine
import com.anthonykamau.foodchap.data.local.entity.MealPlanEntry
import com.anthonykamau.foodchap.data.local.entity.MealType
import com.anthonykamau.foodchap.data.local.entity.RecipeStep
import com.anthonykamau.foodchap.ui.planner.MealPlannerViewModel
import kotlinx.coroutines.launch

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var contentLayout: LinearLayout

    private var accentColor = 0
    private var surfaceColor = 0
    private var textPrimaryColor = 0
    private var textSecondaryColor = 0
    private var backgroundColor = 0
    private var onAccentColor = 0
    private var ratingGoldColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accentColor = ContextCompat.getColor(this, R.color.fc_accent)
        surfaceColor = ContextCompat.getColor(this, R.color.fc_surface)
        textPrimaryColor = ContextCompat.getColor(this, R.color.fc_text_primary)
        textSecondaryColor = ContextCompat.getColor(this, R.color.fc_text_secondary)
        backgroundColor = ContextCompat.getColor(this, R.color.fc_background)
        onAccentColor = ContextCompat.getColor(this, R.color.fc_on_accent)
        ratingGoldColor = ContextCompat.getColor(this, R.color.fc_rating_gold)

        val recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, -1L)
        if (recipeId == -1L) {
            finish()
            return
        }

        val scrollView = ScrollView(this)
        contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 0, 0, 60)
        }
        scrollView.addView(contentLayout)

        val outerLayout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        outerLayout.addView(scrollView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        val bottomNav = BottomNavigationView(this)
        outerLayout.addView(bottomNav, LinearLayout.LayoutParams(
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
        decoratedFrame.addView(outerLayout)
        setContentView(decoratedFrame)

        BottomNavHelper.attach(this, bottomNav, R.id.nav_recipes)

        val factory = RecipeDetailViewModel.Factory(application, recipeId)

        viewModel = ViewModelProvider(this, factory)[RecipeDetailViewModel::class.java]

        viewModel.recipe.observe(this) { recipe ->
            if (recipe != null) {
                addPhotoHeader(recipe.imageResName)
                addHeaderText(recipe.name, 24f, true, textPrimaryColor, 40)
                addInfoRow(recipe.rating, recipe.prepTimeMinutes + recipe.cookTimeMinutes, recipe.totalCostKes, recipe.servings)
                addAddToPlannerButton(recipeId)
                addHeaderText(recipe.description, 14f, false, textSecondaryColor, 40)
            }
        }

        viewModel.ingredients.observe(this) { ingredients ->
            addSectionTitle("Ingredients")
            addIngredientFlow(ingredients)
        }

        viewModel.steps.observe(this) { steps ->
            addSectionTitle("Steps")
            steps.forEach { addStepBlock(it) }
        }

        viewModel.pairings.observe(this) { pairedRecipes ->
            if (pairedRecipes.isNotEmpty()) {
                addPairingSection(pairedRecipes)
            }
        }
    }

    private fun addPairingSection(pairedRecipes: List<com.anthonykamau.foodchap.data.local.entity.Recipe>) {
        addSectionTitle("Goes Well With")
        val horizontalScroll = android.widget.HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
        }
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(40, 0, 40, 0)
        }
        pairedRecipes.forEach { pairedRecipe ->
            val cardBinding = com.anthonykamau.foodchap.databinding.ItemRecipeCompactBinding.inflate(
                layoutInflater, row, false
            )
            cardBinding.compactRecipeName.text = pairedRecipe.name
            cardBinding.compactRecipePrice.text = "KES ${pairedRecipe.totalCostKes}"
            val resId = pairedRecipe.imageResName?.let { resources.getIdentifier(it, "drawable", packageName) }
            if (resId != null && resId != 0) {
                cardBinding.compactRecipeImage.setImageResource(resId)
            }
            cardBinding.root.setOnClickListener { start(this, pairedRecipe.id) }
            row.addView(cardBinding.root)
        }
        horizontalScroll.addView(row)
        contentLayout.addView(horizontalScroll)
    }

    private fun addPhotoHeader(imageResName: String?) {
        val resId = imageResName?.let { resources.getIdentifier(it, "drawable", packageName) }
        val imageView = ImageView(this).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            if (resId != null && resId != 0) {
                setImageResource(resId)
            } else {
                setBackgroundColor(ContextCompat.getColor(this@RecipeDetailActivity, R.color.fc_peach_accent))
            }
        }
        contentLayout.addView(imageView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 480
        ))
    }

    private fun addHeaderText(text: String, sizeSp: Float, bold: Boolean, color: Int, topPadding: Int) {
        contentLayout.addView(TextView(this).apply {
            this.text = text
            textSize = sizeSp
            if (bold) setTypeface(typeface, Typeface.BOLD)
            setTextColor(color)
            setPadding(topPadding, 16, topPadding, 0)
        })
    }

    private fun addInfoRow(rating: Float, totalMinutes: Int, costKes: Int, servings: Int) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(40, 12, 40, 0)
        }
        row.addView(TextView(this).apply {
            text = "★ %.1f".format(rating)
            textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(ratingGoldColor)
        })
        row.addView(TextView(this).apply {
            text = "  •  $totalMinutes min  •  Serves $servings"
            textSize = 14f
            setTextColor(textSecondaryColor)
        })
        contentLayout.addView(row)

        contentLayout.addView(TextView(this).apply {
            text = "KES $costKes"
            textSize = 13f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(onAccentColor)
            background = ContextCompat.getDrawable(this@RecipeDetailActivity, R.drawable.bg_price_pill)
            setPadding(28, 8, 28, 8)
        }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(40, 16, 40, 0)
        })
    }

    private fun addAddToPlannerButton(recipeId: Long) {
        val button = Button(this).apply {
            text = "+ Add to Meal Planner"
            setTextColor(onAccentColor)
            background = ContextCompat.getDrawable(this@RecipeDetailActivity, R.drawable.bg_button_primary)
            setOnClickListener { showAddToPlannerFlow(recipeId) }
        }
        contentLayout.addView(button, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(40, 20, 40, 0) })
    }

    private fun showAddToPlannerFlow(recipeId: Long) {
        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        AlertDialog.Builder(this, R.style.FoodChapAlertDialog)
            .setTitle("Add to which day?")
            .setItems(days.toTypedArray()) { _, dayIndex ->
                val mealTypes = listOf(MealType.BREAKFAST, MealType.LUNCH, MealType.SUPPER)
                val mealLabels = mealTypes.map { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }.toTypedArray()
                AlertDialog.Builder(this, R.style.FoodChapAlertDialog)
                    .setTitle("Which meal?")
                    .setItems(mealLabels) { _, mealIndex ->
                        val mealType = mealTypes[mealIndex]
                        val dayOfWeek = dayIndex + 1
                        lifecycleScope.launch {
                            val mealPlanDao = (application as FoodChapApp).database.mealPlanDao()
                            val weekStart = MealPlannerViewModel.computeThisMondayDateString()
                            val existing = mealPlanDao.getEntryForSlot(weekStart, dayOfWeek, mealType.name)
                            if (existing != null) {
                                mealPlanDao.upsert(existing.copy(recipeId = recipeId))
                            } else {
                                mealPlanDao.upsert(MealPlanEntry(weekStartDate = weekStart, dayOfWeek = dayOfWeek, mealType = mealType, recipeId = recipeId))
                            }
                            Toast.makeText(this@RecipeDetailActivity, "Added to ${days[dayIndex]} ${mealLabels[mealIndex]}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .show()
            }
            .show()
    }

    private fun addSectionTitle(title: String) {
        contentLayout.addView(TextView(this).apply {
            text = title
            textSize = 19f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(accentColor)
            setPadding(40, 48, 40, 12)
        })
    }

    private fun addIngredientFlow(ingredients: List<IngredientLine>) {
        val flowContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 0, 40, 0)
        }
        ingredients.forEach { line ->
            val unitText = line.unit?.let { " $it" } ?: ""
            val row = LinearLayout(this).apply {
                background = ContextCompat.getDrawable(this@RecipeDetailActivity, R.drawable.bg_slot_card)
                setPadding(24, 18, 24, 18)
            }
            row.addView(TextView(this).apply {
                text = "${line.name}  —  ${line.quantity}$unitText"
                textSize = 14f
                setTextColor(textPrimaryColor)
            })
            flowContainer.addView(row, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 8) })
        }
        contentLayout.addView(flowContainer)
    }

    private fun addStepBlock(step: RecipeStep) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(this@RecipeDetailActivity, R.drawable.bg_slot_card)
            setPadding(28, 24, 28, 24)
        }

        card.addView(TextView(this).apply {
            text = "${step.stepNumber}. ${step.instruction}"
            textSize = 15f
            setTextColor(textPrimaryColor)
        })

        if (step.timerSeconds != null) {
            val timerLabel = TextView(this).apply {
                text = formatTime(step.timerSeconds)
                textSize = 14f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(accentColor)
                setPadding(0, 12, 0, 0)
            }
            val timerButton = Button(this).apply {
                text = "Start timer"
                setTextColor(onAccentColor)
                background = ContextCompat.getDrawable(this@RecipeDetailActivity, R.drawable.bg_button_primary)
            }
            val stopButton = Button(this).apply {
                text = "Stop"
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(textSecondaryColor)
                visibility = android.view.View.GONE
            }
            var activeTimer: CountDownTimer? = null

            timerButton.setOnClickListener {
                timerButton.isEnabled = false
                stopButton.visibility = android.view.View.VISIBLE
                activeTimer = object : CountDownTimer(step.timerSeconds * 1000L, 1000L) {
                    override fun onTick(millisUntilFinished: Long) {
                        timerLabel.text = formatTime((millisUntilFinished / 1000L).toInt())
                    }
                    override fun onFinish() {
                        timerLabel.text = "Done!"
                        timerButton.isEnabled = true
                        stopButton.visibility = android.view.View.GONE
                    }
                }.start()
            }
            stopButton.setOnClickListener {
                activeTimer?.cancel()
                timerLabel.text = formatTime(step.timerSeconds)
                timerButton.isEnabled = true
                stopButton.visibility = android.view.View.GONE
            }

            val buttonRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            buttonRow.addView(timerButton)
            buttonRow.addView(stopButton)

            card.addView(timerLabel)
            card.addView(buttonRow, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.START; topMargin = 12 })
        }

        contentLayout.addView(card, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(40, 0, 40, 12) })
    }

    private fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }

    companion object {
        private const val EXTRA_RECIPE_ID = "extra_recipe_id"

        fun start(context: Context, recipeId: Long) {
            context.startActivity(Intent(context, RecipeDetailActivity::class.java).apply {
                putExtra(EXTRA_RECIPE_ID, recipeId)
            })
        }
    }
}