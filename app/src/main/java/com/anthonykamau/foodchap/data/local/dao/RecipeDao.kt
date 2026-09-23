package com.anthonykamau.foodchap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.anthonykamau.foodchap.data.local.entity.Recipe
import com.anthonykamau.foodchap.data.local.entity.RecipeIngredient
import com.anthonykamau.foodchap.data.local.entity.RecipeStep
import kotlinx.coroutines.flow.Flow

/** Recipe Results row: a recipe plus how many/what % of its ingredients the user already has. */
/** Recipe Results row: a recipe plus how many/what % of its ingredients the user already has. */
data class RecipeMatch(
    val recipe: Recipe,
    val totalIngredients: Int,
    val matchedIngredients: Int,
    val matchedIngredientNames: List<String> = emptyList(),
    val missingIngredientNames: List<String> = emptyList()
) {
    val matchPercent: Int
        get() = if (totalIngredients == 0) 0 else (matchedIngredients * 100) / totalIngredients
}
data class IngredientLine(
    val ingredientId: Long,
    val name: String,
    val quantity: String,
    val unit: String?,
    val category: com.anthonykamau.foodchap.data.local.entity.IngredientCategory,
    val standardizedAmount: Double?,
    val standardizedUnit: String?
)

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredients(items: List<RecipeIngredient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeSteps(steps: List<RecipeStep>)

    @Query("""
        SELECT i.id AS ingredientId, i.name AS name, ri.quantity AS quantity, ri.unit AS unit,
               i.category AS category, ri.standardizedAmount AS standardizedAmount, ri.standardizedUnit AS standardizedUnit
        FROM recipe_ingredients ri
        INNER JOIN ingredients i ON i.id = ri.ingredientId
        WHERE ri.recipeId = :recipeId
    """)
    suspend fun getIngredientDetailsForRecipe(recipeId: Long): List<IngredientLine>

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Long): Recipe?

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun getIngredientsForRecipe(recipeId: Long): List<RecipeIngredient>

    @Query("SELECT * FROM recipe_steps WHERE recipeId = :recipeId ORDER BY stepNumber ASC")
    suspend fun getStepsForRecipe(recipeId: Long): List<RecipeStep>

    @Query("""
        SELECT name FROM recipes
        WHERE mealType = :mealType
        ORDER BY RANDOM() LIMIT :limit
    """)
    suspend fun getRandomNamesForMealType(mealType: String, limit: Int): List<String>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): Flow<List<Recipe>>


    /**
     * Core "What Can I Cook?" query. Given a set of ingredient IDs the
     * user has on hand, returns every recipe with its total ingredient
     * count and how many of those ingredients were matched. Sort by
     * matchPercent descending in the repository/ViewModel layer once
     * mapped to RecipeMatch.
     */
    @Transaction
    suspend fun getMatchesForIngredients(selectedIngredientIds: List<Long>): List<RecipeMatch> {
        val allRecipes = getAllRecipesOnce()
        return allRecipes.map { recipe ->
            val required = getIngredientDetailsForRecipe(recipe.id)
            val matched = required.filter { it.ingredientId in selectedIngredientIds }
            val missing = required.filter { it.ingredientId !in selectedIngredientIds }
            RecipeMatch(
                recipe = recipe,
                totalIngredients = required.size,
                matchedIngredients = matched.size,
                matchedIngredientNames = matched.map { it.name },
                missingIngredientNames = missing.map { it.name }
            )
        }
    }

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipesOnce(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipePairings(pairings: List<com.anthonykamau.foodchap.data.local.entity.RecipePairing>)

    @Query("""
        SELECT r.* FROM recipes r
        INNER JOIN recipe_pairings rp ON rp.pairedRecipeId = r.id
        WHERE rp.recipeId = :recipeId
        ORDER BY r.rating DESC
        LIMIT 10
    """)
    suspend fun getPairingsForRecipe(recipeId: Long): List<Recipe>
}
