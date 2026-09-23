package com.anthonykamau.foodchap.ui.planner

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anthonykamau.foodchap.MainActivity
import com.anthonykamau.foodchap.R
import com.anthonykamau.foodchap.data.local.entity.MealPlanEntry
import com.anthonykamau.foodchap.data.local.entity.MealType
import com.anthonykamau.foodchap.data.local.entity.Recipe
import com.anthonykamau.foodchap.ui.BottomNavHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MealPlannerActivity : AppCompatActivity() {

    private lateinit var viewModel: MealPlannerViewModel
    private lateinit var gridContainer: LinearLayout
    private lateinit var progressText: TextView

    private var pendingDay: Int = -1
    private var pendingMealType: MealType? = null

    private var lastEntries: List<MealPlanEntry> = emptyList()
    private var lastRecipesById: Map<Long, Recipe> = emptyMap()

    private var accentColor = 0
    private var surfaceColor = 0
    private var textPrimaryColor = 0
    private var textSecondaryColor = 0
    private var backgroundColor = 0
    private var onAccentColor = 0

    private val pickRecipeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val recipeId = result.data?.getLongExtra(MainActivity.EXTRA_SELECTED_RECIPE_ID, -1L) ?: -1L
            val mealType = pendingMealType
            if (recipeId != -1L && mealType != null && pendingDay != -1) {
                viewModel.assignRecipe(pendingDay, mealType, recipeId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accentColor = ContextCompat.getColor(this, R.color.fc_accent)
        surfaceColor = ContextCompat.getColor(this, R.color.fc_surface)
        textPrimaryColor = ContextCompat.getColor(this, R.color.fc_text_primary)
        textSecondaryColor = ContextCompat.getColor(this, R.color.fc_text_secondary)
        backgroundColor = ContextCompat.getColor(this, R.color.fc_background)
        onAccentColor = ContextCompat.getColor(this, R.color.fc_on_accent)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val title = TextView(this).apply {
            text = "Meal Planner"
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(textPrimaryColor)
            setPadding(40, 28, 40, 4)
        }
        rootLayout.addView(title)

        progressText = TextView(this).apply {
            textSize = 13f
            setTextColor(accentColor)
            setTypeface(typeface, Typeface.BOLD)
            setPadding(40, 0, 40, 4)
        }
        rootLayout.addView(progressText)

        val subtitle = TextView(this).apply {
            text = "Tap a slot to assign a recipe, tap again to clear it"
            textSize = 13f
            setTextColor(textSecondaryColor)
            setPadding(40, 0, 40, 12)
        }
        rootLayout.addView(subtitle)

        val scrollView = ScrollView(this)
        gridContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 0, 32, 32)
        }
        scrollView.addView(gridContainer)
        rootLayout.addView(scrollView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

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

        BottomNavHelper.attach(this, bottomNav, R.id.nav_planner)

        viewModel = ViewModelProvider(this)[MealPlannerViewModel::class.java]

        viewModel.entries.observe(this) { entries ->
            lastEntries = entries
            renderGrid()
        }
        viewModel.recipesById.observe(this) { map ->
            lastRecipesById = map
            renderGrid()
        }
    }

    private fun mealIcon(mealType: MealType): String = when (mealType) {
        MealType.BREAKFAST -> "🌅"
        MealType.LUNCH -> "☀️"
        MealType.SUPPER -> "🌙"
        MealType.SNACK -> "🍽️"
    }

    private fun renderGrid() {
        gridContainer.removeAllViews()

        val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val mealTypes = listOf(MealType.BREAKFAST, MealType.LUNCH, MealType.SUPPER)
        val totalSlots = dayNames.size * mealTypes.size
        progressText.text = "${lastEntries.size} of $totalSlots meals planned this week"

        for (dayIndex in 1..7) {
            val dayHeader = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(4, 28, 4, 10)
            }
            dayHeader.addView(TextView(this).apply {
                text = dayNames[dayIndex - 1].take(3).uppercase()
                textSize = 12f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(onAccentColor)
                background = ContextCompat.getDrawable(this@MealPlannerActivity, R.drawable.bg_price_pill)
                setPadding(20, 6, 20, 6)
            })
            dayHeader.addView(TextView(this).apply {
                text = "  ${dayNames[dayIndex - 1]}"
                textSize = 16f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(textPrimaryColor)
                gravity = android.view.Gravity.CENTER_VERTICAL
            })
            gridContainer.addView(dayHeader)

            for (mealType in mealTypes) {
                val entry = lastEntries.find { it.dayOfWeek == dayIndex && it.mealType == mealType }
                val recipe = entry?.let { lastRecipesById[it.recipeId] }

                val card = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    background = ContextCompat.getDrawable(this@MealPlannerActivity, R.drawable.bg_slot_card)
                    setPadding(20, 16, 24, 16)
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }

                val thumb = ImageView(this).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    val resId = recipe?.imageResName?.let { resources.getIdentifier(it, "drawable", packageName) }
                    if (resId != null && resId != 0) {
                        setImageResource(resId)
                    } else {
                        setImageDrawable(null)
                        setBackgroundColor(if (recipe != null) ContextCompat.getColor(this@MealPlannerActivity, R.color.fc_peach_accent) else android.graphics.Color.TRANSPARENT)
                    }
                }
                card.addView(thumb, LinearLayout.LayoutParams(52, 52).apply {
                    marginEnd = 20
                })

                val textColumn = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }
                textColumn.addView(TextView(this).apply {
                    text = "${mealIcon(mealType)}  ${mealType.name.lowercase().replaceFirstChar { it.uppercase() }}"
                    textSize = 12f
                    setTextColor(textSecondaryColor)
                })
                textColumn.addView(TextView(this).apply {
                    text = recipe?.name ?: "+ Add recipe"
                    textSize = 15f
                    setTypeface(typeface, if (recipe != null) Typeface.BOLD else Typeface.NORMAL)
                    setTextColor(if (recipe != null) textPrimaryColor else accentColor)
                    setPadding(0, 2, 0, 0)
                })
                card.addView(textColumn)

                card.setOnClickListener {
                    if (entry != null) {
                        viewModel.clearSlot(entry)
                    } else {
                        pendingDay = dayIndex
                        pendingMealType = mealType
                        pickRecipeLauncher.launch(MainActivity.createPickIntent(this))
                    }
                }

                gridContainer.addView(card, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 10) })
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MealPlannerActivity::class.java))
        }
    }
}