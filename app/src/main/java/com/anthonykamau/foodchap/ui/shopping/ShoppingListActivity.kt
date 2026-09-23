package com.anthonykamau.foodchap.ui.shopping

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anthonykamau.foodchap.R
import com.anthonykamau.foodchap.data.local.entity.IngredientCategory
import com.anthonykamau.foodchap.data.local.entity.ShoppingListItem
import com.anthonykamau.foodchap.ui.BottomNavHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var listContainer: LinearLayout
    private lateinit var totalText: TextView

    private var accentColor = 0
    private var surfaceColor = 0
    private var textPrimaryColor = 0
    private var textSecondaryColor = 0
    private var backgroundColor = 0
    private var onAccentColor = 0

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

        rootLayout.addView(TextView(this).apply {
            text = "Shopping List"
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(textPrimaryColor)
            setPadding(40, 28, 40, 4)
        })

        rootLayout.addView(TextView(this).apply {
            text = "Generated from this week's meal plan"
            textSize = 13f
            setTextColor(textSecondaryColor)
            setPadding(40, 0, 40, 12)
        })

        totalText = TextView(this).apply {
            text = "Estimated total: KES 0"
            textSize = 15f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(onAccentColor)
            background = ContextCompat.getDrawable(this@ShoppingListActivity, R.drawable.bg_price_pill)
            setPadding(28, 10, 28, 10)
        }
        rootLayout.addView(totalText, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(40, 0, 40, 20) })

        val regenerateButton = Button(this).apply {
            text = "Regenerate from This Week's Plan"
            setTextColor(onAccentColor)
            background = ContextCompat.getDrawable(this@ShoppingListActivity, R.drawable.bg_button_primary)
        }
        rootLayout.addView(regenerateButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(40, 0, 40, 20) })

        val scrollView = ScrollView(this)
        listContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 0, 40, 40)
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

        BottomNavHelper.attach(this, bottomNav, R.id.nav_shopping)

        viewModel = ViewModelProvider(this)[ShoppingListViewModel::class.java]
        regenerateButton.setOnClickListener { viewModel.regenerateFromPlan() }
        viewModel.items.observe(this) { items -> renderItems(items) }
        viewModel.estimatedTotalKes.observe(this) { total ->
            totalText.text = "Estimated total: KES $total"
        }
    }

    private fun categoryIcon(category: IngredientCategory): String = when (category) {
        IngredientCategory.STAPLE -> "🌾"
        IngredientCategory.VEGETABLE -> "🥬"
        IngredientCategory.PROTEIN -> "🍗"
        IngredientCategory.SPICE -> "🌶️"
        IngredientCategory.DAIRY -> "🥛"
        IngredientCategory.OTHER -> "🧺"
    }

    private fun renderItems(items: List<ShoppingListItem>) {
        listContainer.removeAllViews()

        if (items.isEmpty()) {
            listContainer.addView(TextView(this).apply {
                text = "No items yet. Assign recipes in Meal Planner, then tap Regenerate above."
                textSize = 14f
                setTextColor(textSecondaryColor)
                setPadding(0, 20, 0, 20)
            })
            return
        }

        val grouped = items.groupBy { it.category }
        for (category in IngredientCategory.entries) {
            val categoryItems = grouped[category] ?: continue
            listContainer.addView(TextView(this).apply {
                text = "${categoryIcon(category)}  ${category.name.lowercase().replaceFirstChar { it.uppercase() }}"
                textSize = 16f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(accentColor)
                setPadding(8, 28, 8, 10)
            })
            categoryItems.forEach { addItemRow(it) }
        }
    }

    private fun addItemRow(item: ShoppingListItem) {
        val unitText = item.unit?.let { " $it" } ?: ""
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(this@ShoppingListActivity, R.drawable.bg_slot_card)
            setPadding(20, 8, 28, 8)
        }
        card.addView(CheckBox(this).apply {
            text = "${item.ingredientName} — ${item.quantity}$unitText"
            textSize = 15f
            setTextColor(textPrimaryColor)
            isChecked = item.isChecked
            if (item.isChecked) paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            setOnCheckedChangeListener { _, _ -> viewModel.toggleChecked(item) }
        })

        val priceLabel = TextView(this).apply {
            textSize = 12f
            setTextColor(textSecondaryColor)
            setPadding(48, 0, 0, 6)
        }
        card.addView(priceLabel)
        lifecycleScope.launch {
            val estimate = viewModel.estimateCostForItem(item)
            priceLabel.text = if (estimate != null) "~KES $estimate" else "Price unavailable"
        }

        listContainer.addView(card, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 0, 0, 10) })
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ShoppingListActivity::class.java))
        }
    }
}
