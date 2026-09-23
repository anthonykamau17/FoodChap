package com.anthonykamau.foodchap.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe_pairings",
    foreignKeys = [
        ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["pairedRecipeId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("recipeId"), Index("pairedRecipeId")]
)
data class RecipePairing(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: Long,
    val pairedRecipeId: Long
)