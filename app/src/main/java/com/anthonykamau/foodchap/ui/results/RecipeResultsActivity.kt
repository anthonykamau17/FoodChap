package com.anthonykamau.foodchap.ui.results

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anthonykamau.foodchap.R
import com.anthonykamau.foodchap.ui.BottomNavHelper
import com.anthonykamau.foodchap.ui.detail.RecipeDetailActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeResultsActivity : AppCompatActivity() {

    private lateinit var listContainer: LinearLayout

    private var accentColor = 0
    private var surfaceColor = 0
    private var textPrimaryColor = 0
    private var textSecondaryColor = 0
    private var backgroundColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accentColor = ContextCompat.getColor(this, R.color.fc_accent)
        surfaceColor = ContextCompat.getColor(this, R.color.fc_surface)
        textPrimaryColor = ContextCompat.getColor(this, R.color.fc_text_primary)
        textSecondaryColor = ContextCompat.getColor(this, R.color.fc_text_secondary)
        backgroundColor = ContextCompat.getColor(this, R.color.fc_background)

        val ids = intent.getLongArrayExtra(EXTRA_INGREDIENT_IDS)?.toList() ?: emptyList()
        val pkgIngredientIds = intent.getLongArrayExtra(EXTRA_PACKAGE_INGREDIENT_IDS) ?: LongArray(0)
        val pkgIds = intent.getLongArrayExtra(EXTRA_PACKAGE_IDS) ?: LongArray(0)
        val packageMap = pkgIngredientIds.zip(pkgIds.toList()).toMap()

        val rootLayout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

        val scrollView = ScrollView(this)
        listContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }
        scrollView.addView(listContainer)
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

        BottomNavHelper.attach(this, bottomNav, R.id.nav_cook)

        val title = TextView(this).apply {
            text = "What Can I Cook?"
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(textPrimaryColor)
            setPadding(0, 0, 0, 24)
        }
        listContainer.addView(title)

        val factory = RecipeResultsViewModel.Factory(application, ids, packageMap)
        val viewModel = ViewModelProvider(this, factory)[RecipeResultsViewModel::class.java]

        viewModel.results.observe(this) { rows ->
            if (rows.isEmpty()) {
                listContainer.addView(TextView(this).apply {
                    text = "No recipes match at least half of your selected ingredients. Try selecting a few more."
                    textSize = 14f
                    setTextColor(textSecondaryColor)
                    setPadding(0, 20, 0, 20)
                })
            } else {
                rows.forEach { addResultRow(it) }
            }
        }
    }

    private fun addResultRow(row: RecipeResultRow) {
        val match = row.match
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(this@RecipeResultsActivity, R.drawable.bg_slot_card)
            setPadding(28, 24, 28, 24)
            setOnClickListener {
                RecipeDetailActivity.start(this@RecipeResultsActivity, match.recipe.id)
            }
        }

        card.addView(TextView(this).apply {
            text = "${match.matchPercent}% match  —  ${match.recipe.name}"
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(textPrimaryColor)
        })

        card.addView(TextView(this).apply {
            text = "★ ${match.recipe.rating}  •  ${match.recipe.prepTimeMinutes + match.recipe.cookTimeMinutes} min  •  KES ${match.recipe.totalCostKes}"
            textSize = 13f
            setTextColor(textSecondaryColor)
            setPadding(0, 4, 0, 0)
        })

        if (match.matchedIngredientNames.isNotEmpty()) {
            card.addView(TextView(this).apply {
                text = "✓ You have: ${match.matchedIngredientNames.joinToString(", ")}"
                textSize = 12f
                setTextColor(accentColor)
                setPadding(0, 8, 0, 0)
            })
        }

        if (match.missingIngredientNames.isNotEmpty()) {
            card.addView(TextView(this).apply {
                text = "Still need: ${match.missingIngredientNames.joinToString(", ")}"
                textSize = 12f
                setTextColor(textSecondaryColor)
                setPadding(0, 4, 0, 0)
            })
        }

        if (row.yieldBatches != null) {
            card.addView(TextView(this).apply {
                text = if (row.yieldBatches > 0) {
                    "With what you selected: about ${row.yieldBatches} batch(es), ~${row.yieldBatches * match.recipe.servings} servings"
                } else {
                    "Not quite enough of a priced ingredient to make a full batch"
                }
                textSize = 13f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(accentColor)
                setPadding(0, 8, 0, 0)
            })
        }

        listContainer.addView(card, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 0, 0, 12) })
    }

    companion object {
        private const val EXTRA_INGREDIENT_IDS = "extra_ingredient_ids"
        private const val EXTRA_PACKAGE_INGREDIENT_IDS = "extra_package_ingredient_ids"
        private const val EXTRA_PACKAGE_IDS = "extra_package_ids"

        fun start(context: Context, ingredientIds: LongArray, packageIngredientIds: LongArray, packageIds: LongArray) {
            context.startActivity(Intent(context, RecipeResultsActivity::class.java).apply {
                putExtra(EXTRA_INGREDIENT_IDS, ingredientIds)
                putExtra(EXTRA_PACKAGE_INGREDIENT_IDS, packageIngredientIds)
                putExtra(EXTRA_PACKAGE_IDS, packageIds)
            })
        }
    }
}