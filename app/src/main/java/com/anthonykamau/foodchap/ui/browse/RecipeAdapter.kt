package com.anthonykamau.foodchap.ui.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonykamau.foodchap.R
import com.anthonykamau.foodchap.data.local.entity.Recipe
import com.anthonykamau.foodchap.databinding.ItemRecipeBinding


class RecipeAdapter(
    private val onClick: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe, onClick: (Recipe) -> Unit) {
            val context = binding.root.context
            val resId = recipe.imageResName?.let {
                context.resources.getIdentifier(it, "drawable", context.packageName)
            }
            if (resId != null && resId != 0) {
                binding.recipeImage.setImageResource(resId)
            } else {
                binding.recipeImage.setImageDrawable(null)
            }

            binding.recipeName.text = recipe.name
            binding.recipeRating.text = "★ %.1f".format(recipe.rating)
            binding.recipeTime.text = "${recipe.prepTimeMinutes + recipe.cookTimeMinutes} min"
            binding.recipePrice.text = "KES ${recipe.totalCostKes}"
            binding.root.setOnClickListener { onClick(recipe) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
        }
    }
}