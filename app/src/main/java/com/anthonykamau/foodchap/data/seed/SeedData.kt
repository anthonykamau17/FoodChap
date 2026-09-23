package com.anthonykamau.foodchap.data.seed

import androidx.room.withTransaction
import com.anthonykamau.foodchap.data.local.AppDatabase
import com.anthonykamau.foodchap.data.local.entity.Difficulty
import com.anthonykamau.foodchap.data.local.entity.Ingredient
import com.anthonykamau.foodchap.data.local.entity.IngredientCategory
import com.anthonykamau.foodchap.data.local.entity.MealType
import com.anthonykamau.foodchap.data.local.entity.Recipe
import com.anthonykamau.foodchap.data.local.entity.RecipeIngredient
import com.anthonykamau.foodchap.data.local.entity.RecipeStep

/**
 * Populates the database with the master ingredient catalog and 8 starter
 * recipes on first launch. Call SeedData.populateIfEmpty(db) once, e.g.
 * from a splash screen or Application.onCreate() inside a coroutine.
 */
object SeedData {

    suspend fun populateIfEmpty(db: AppDatabase) {
        val existing = db.recipeDao().getAllRecipesOnce()
        if (existing.isNotEmpty()) return // already seeded, don't duplicate

        db.withTransaction {
            populateInternal(db)
        }
    }

    private suspend fun populateInternal(db: AppDatabase) {

        // ---- 1. Ingredient catalog (used by the Ingredient Picker) ----
        val catalog = listOf(
            Ingredient(name = "Maize flour (unga)", category = IngredientCategory.STAPLE),
            Ingredient(name = "Wheat flour", category = IngredientCategory.STAPLE),
            Ingredient(name = "Rice", category = IngredientCategory.STAPLE),
            Ingredient(name = "Potatoes", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Sukuma wiki (kale)", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Tomatoes", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Onions", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Carrots", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Green pepper", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Coriander (dhania)", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Green maize (fresh)", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Kidney beans", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Beef", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Goat meat", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Chicken", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Eggs", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Cooking oil", category = IngredientCategory.OTHER),
            Ingredient(name = "Salt", category = IngredientCategory.SPICE),
            Ingredient(name = "Royco / beef masala", category = IngredientCategory.SPICE),
            Ingredient(name = "Curry powder", category = IngredientCategory.SPICE),
            Ingredient(name = "Garlic", category = IngredientCategory.SPICE),
            Ingredient(name = "Ginger", category = IngredientCategory.SPICE),
            Ingredient(name = "Cardamom", category = IngredientCategory.SPICE),
            Ingredient(name = "Cinnamon", category = IngredientCategory.SPICE),
            Ingredient(name = "Milk", category = IngredientCategory.DAIRY),
            Ingredient(name = "Sugar", category = IngredientCategory.OTHER),
            Ingredient(name = "Tea leaves", category = IngredientCategory.OTHER),
            Ingredient(name = "Lemon", category = IngredientCategory.OTHER),
            Ingredient(name = "Baking powder", category = IngredientCategory.OTHER),
            Ingredient(name = "Coconut milk", category = IngredientCategory.DAIRY),
            Ingredient(name = "Green grams (ndengu)", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Gram flour (besan)", category = IngredientCategory.STAPLE),
            Ingredient(name = "Peas", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Managu (African nightshade)", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Omena (dried fish)", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Green bananas (matoke)", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Cowpea leaves (kunde)", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Terere (amaranth greens)", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Fish (tilapia)", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Turmeric", category = IngredientCategory.SPICE),
            Ingredient(name = "Yeast", category = IngredientCategory.OTHER),
            Ingredient(name = "Pasta", category = IngredientCategory.STAPLE),
            Ingredient(name = "Bread", category = IngredientCategory.STAPLE),
            Ingredient(name = "Butter", category = IngredientCategory.DAIRY),
            Ingredient(name = "Cheese", category = IngredientCategory.DAIRY),
            Ingredient(name = "Yogurt", category = IngredientCategory.DAIRY),
            Ingredient(name = "Bell pepper", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Mushrooms", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Broccoli", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Spinach", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Cucumber", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Lettuce", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Avocado", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Lime", category = IngredientCategory.OTHER),
            Ingredient(name = "Basil", category = IngredientCategory.SPICE),
            Ingredient(name = "Oregano", category = IngredientCategory.SPICE),
            Ingredient(name = "Paprika", category = IngredientCategory.SPICE),
            Ingredient(name = "Cumin", category = IngredientCategory.SPICE),
            Ingredient(name = "Black pepper", category = IngredientCategory.SPICE),
            Ingredient(name = "Soy sauce", category = IngredientCategory.OTHER),
            Ingredient(name = "Vinegar", category = IngredientCategory.OTHER),
            Ingredient(name = "Honey", category = IngredientCategory.OTHER),
            Ingredient(name = "Oats", category = IngredientCategory.STAPLE),
            Ingredient(name = "Lentils", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Chickpeas", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Pork", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Bacon", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Apples", category = IngredientCategory.OTHER),
            Ingredient(name = "Bananas", category = IngredientCategory.OTHER),
            Ingredient(name = "Baobab fruit (mabuyu)", category = IngredientCategory.OTHER),
            Ingredient(name = "Millet flour", category = IngredientCategory.STAPLE),
            Ingredient(name = "Cassava flour", category = IngredientCategory.STAPLE),
            Ingredient(name = "Dried maize (muthokoi)", category = IngredientCategory.STAPLE),
            Ingredient(name = "Groundnuts (njugu)", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Ripe plantains", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Dried maize (muthokoi)", category = IngredientCategory.STAPLE),
            Ingredient(name = "Tripe (matumbo)", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Cabbage", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Beetroot", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Pumpkin", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Sweet potatoes", category = IngredientCategory.VEGETABLE),
            Ingredient(name = "Mango", category = IngredientCategory.OTHER),
            Ingredient(name = "Pineapple", category = IngredientCategory.OTHER),
            Ingredient(name = "Orange", category = IngredientCategory.OTHER),
            Ingredient(name = "Watermelon", category = IngredientCategory.OTHER),
            Ingredient(name = "Vanilla essence", category = IngredientCategory.OTHER),
            Ingredient(name = "Cocoa powder", category = IngredientCategory.OTHER),
            Ingredient(name = "Chili peppers", category = IngredientCategory.SPICE),
            Ingredient(name = "Sausages", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Prawns", category = IngredientCategory.PROTEIN),
            Ingredient(name = "Almonds", category = IngredientCategory.OTHER),
        )
        val ids = db.ingredientDao().insertAll(catalog)
        // map name -> generated id for building RecipeIngredient rows below
        val idOf = catalog.mapIndexed { i, ing -> ing.name to ids[i] }.toMap()

        // ---- 1b. Package prices (Kenyan market, mid-2026, approximate) ----
        fun pkg(ingredientName: String, sizeLabel: String, amount: Double, unit: String, priceKes: Int) =
            com.anthonykamau.foodchap.data.local.entity.IngredientPackage(
                ingredientId = idOf.getValue(ingredientName),
                sizeLabel = sizeLabel,
                amount = amount,
                unit = unit,
                priceKes = priceKes
            )

        val ingredientPackages = listOf(
            pkg("Maize flour (unga)", "1kg", 1000.0, "g", 95),
            pkg("Maize flour (unga)", "2kg", 2000.0, "g", 180),
            pkg("Wheat flour", "1kg", 1000.0, "g", 90),
            pkg("Wheat flour", "2kg", 2000.0, "g", 170),
            pkg("Rice", "1kg", 1000.0, "g", 150),
            pkg("Rice", "2kg", 2000.0, "g", 290),
            pkg("Potatoes", "1kg", 1000.0, "g", 60),
            pkg("Potatoes", "2kg", 2000.0, "g", 110),
            pkg("Sukuma wiki (kale)", "1 bunch", 1.0, "bunch", 10),
            pkg("Tomatoes", "1 piece", 1.0, "piece", 10),
            pkg("Onions", "1 piece", 1.0, "piece", 10),
            pkg("Carrots", "1 piece", 1.0, "piece", 10),
            pkg("Green pepper", "1 piece", 1.0, "piece", 15),
            pkg("Coriander (dhania)", "1 handful", 1.0, "handful", 10),
            pkg("Kidney beans", "1kg", 1000.0, "g", 180),
            pkg("Kidney beans", "2kg", 2000.0, "g", 350),
            pkg("Beef", "1kg", 1000.0, "g", 650),
            pkg("Goat meat", "1kg", 1000.0, "g", 700),
            pkg("Chicken", "1 whole", 1.0, "piece", 500),
            pkg("Eggs", "1 piece", 1.0, "piece", 15),
            pkg("Eggs", "Tray (30)", 30.0, "piece", 420),
            pkg("Cooking oil", "500ml", 500.0, "ml", 150),
            pkg("Cooking oil", "1L", 1000.0, "ml", 280),
            pkg("Cooking oil", "2L", 2000.0, "ml", 550),
            pkg("Cooking oil", "5L", 5000.0, "ml", 1350),
            pkg("Salt", "500g", 500.0, "g", 30),
            pkg("Royco / beef masala", "1 cube", 1.0, "cube", 10),
            pkg("Curry powder", "50g", 50.0, "g", 50),
            pkg("Cinnamon", "1 stick", 1.0, "stick", 10),
            pkg("Milk", "500ml", 500.0, "ml", 65),
            pkg("Milk", "1L", 1000.0, "ml", 120),
            pkg("Milk", "2L", 2000.0, "ml", 230),
            pkg("Sugar", "1kg", 1000.0, "g", 165),
            pkg("Sugar", "2kg", 2000.0, "g", 320),
            pkg("Tea leaves", "100g", 100.0, "g", 90),
            pkg("Lemon", "1 piece", 1.0, "piece", 15),
            pkg("Baking powder", "100g", 100.0, "g", 60),
            pkg("Coconut milk", "400ml tin", 400.0, "ml", 120),
            pkg("Green grams (ndengu)", "1kg", 1000.0, "g", 200),
            pkg("Gram flour (besan)", "500g", 500.0, "g", 90),
            pkg("Peas", "500g", 500.0, "g", 150),
            pkg("Managu (African nightshade)", "1 bunch", 1.0, "bunch", 15),
            pkg("Omena (dried fish)", "250g", 250.0, "g", 150),
            pkg("Green bananas (matoke)", "1 piece", 1.0, "piece", 15),
            pkg("Cowpea leaves (kunde)", "1 bunch", 1.0, "bunch", 15),
            pkg("Terere (amaranth greens)", "1 bunch", 1.0, "bunch", 15),
            pkg("Fish (tilapia)", "1 whole", 1.0, "piece", 350),
            pkg("Turmeric", "50g", 50.0, "g", 40),
            pkg("Yeast", "1 sachet", 11.0, "g", 20),
            pkg("Pasta", "500g", 500.0, "g", 150),
            pkg("Pasta", "1kg", 1000.0, "g", 280),
            pkg("Bread", "1 loaf", 1.0, "piece", 65),
            pkg("Butter", "250g", 250.0, "g", 250),
            pkg("Cheese", "200g", 200.0, "g", 350),
            pkg("Yogurt", "500ml", 500.0, "ml", 150),
            pkg("Bell pepper", "1 piece", 1.0, "piece", 30),
            pkg("Mushrooms", "200g", 200.0, "g", 150),
            pkg("Broccoli", "1 piece", 1.0, "piece", 100),
            pkg("Spinach", "1 bunch", 1.0, "bunch", 15),
            pkg("Cucumber", "1 piece", 1.0, "piece", 20),
            pkg("Lettuce", "1 piece", 1.0, "piece", 60),
            pkg("Avocado", "1 piece", 1.0, "piece", 20),
            pkg("Lime", "1 piece", 1.0, "piece", 10),
            pkg("Basil", "20g", 20.0, "g", 60),
            pkg("Oregano", "20g", 20.0, "g", 80),
            pkg("Paprika", "50g", 50.0, "g", 70),
            pkg("Cumin", "50g", 50.0, "g", 70),
            pkg("Black pepper", "50g", 50.0, "g", 90),
            pkg("Soy sauce", "150ml", 150.0, "ml", 120),
            pkg("Vinegar", "500ml", 500.0, "ml", 90),
            pkg("Honey", "250ml", 250.0, "ml", 350),
            pkg("Oats", "500g", 500.0, "g", 180),
            pkg("Lentils", "500g", 500.0, "g", 130),
            pkg("Lentils", "1kg", 1000.0, "g", 250),
            pkg("Chickpeas", "500g", 500.0, "g", 150),
            pkg("Pork", "1kg", 1000.0, "g", 600),
            pkg("Bacon", "250g", 250.0, "g", 350),
            pkg("Apples", "1 piece", 1.0, "piece", 25),
            pkg("Bananas", "1 piece", 1.0, "piece", 10),
            pkg("Baobab fruit (mabuyu)", "200g", 200.0, "g", 50),
            pkg("Millet flour", "1kg", 1000.0, "g", 150),
            pkg("Cassava flour", "1kg", 1000.0, "g", 100),
            pkg("Groundnuts (njugu)", "500g", 500.0, "g", 150),
            pkg("Ripe plantains", "1 piece", 1.0, "piece", 15),
            pkg("Dried maize (muthokoi)", "1kg", 1000.0, "g", 120),
            pkg("Tripe (matumbo)", "1kg", 1000.0, "g", 300),
            pkg("Cabbage", "1 piece", 1.0, "piece", 40),
            pkg("Beetroot", "1 piece", 1.0, "piece", 20),
            pkg("Pumpkin", "1 piece", 1.0, "piece", 80),
            pkg("Sweet potatoes", "1 piece", 1.0, "piece", 25),
            pkg("Mango", "1 piece", 1.0, "piece", 20),
            pkg("Pineapple", "1 piece", 1.0, "piece", 100),
            pkg("Orange", "1 piece", 1.0, "piece", 15),
            pkg("Watermelon", "1 piece", 1.0, "piece", 150),
            pkg("Vanilla essence", "50ml", 50.0, "ml", 100),
            pkg("Cocoa powder", "100g", 100.0, "g", 150),
            pkg("Chili peppers", "100g", 100.0, "g", 30),
            pkg("Sausages", "500g", 500.0, "g", 250),
            pkg("Prawns", "500g", 500.0, "g", 450),
            pkg("Almonds", "200g", 200.0, "g", 250),
        )
        db.ingredientPackageDao().insertAll(ingredientPackages)

        // ---- 2. Recipes ----
        val recipes = listOf(
            Recipe(
                name = "Ugali na Sukuma Wiki",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 10,
                cookTimeMinutes = 25,
                difficulty = Difficulty.EASY,
                rating = 4.8f,
                totalCostKes = 150,
                servings = 4,
                imageResName = "ugali_sukuma",
                description = "The everyday Kenyan staple — firm maize meal alongside sautéed kale."
            ),
            Recipe(
                name = "Chapati",
                mealType = MealType.LUNCH,
                prepTimeMinutes = 30,
                cookTimeMinutes = 20,
                difficulty = Difficulty.MEDIUM,
                rating = 4.9f,
                totalCostKes = 200,
                servings = 6,
                imageResName = "chapati",
                description = "Soft, layered flatbread — a weekend favourite alongside stew or beans."
            ),
            Recipe(
                name = "Githeri",
                mealType = MealType.LUNCH,
                prepTimeMinutes = 15,
                cookTimeMinutes = 40,
                difficulty = Difficulty.EASY,
                rating = 4.5f,
                totalCostKes = 100,
                servings = 5,
                imageResName = "githeri",
                description = "Boiled maize and beans, simmered with vegetables — filling and budget-friendly."
            ),
            Recipe(
                name = "Pilau",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 15,
                cookTimeMinutes = 45,
                difficulty = Difficulty.MEDIUM,
                rating = 4.9f,
                totalCostKes = 300,
                servings = 5,
                imageResName = "pilau",
                description = "Spiced rice cooked with beef and warming pilau masala — celebration food."
            ),
            Recipe(
                name = "Mandazi",
                mealType = MealType.BREAKFAST,
                prepTimeMinutes = 20,
                cookTimeMinutes = 15,
                difficulty = Difficulty.MEDIUM,
                rating = 4.7f,
                totalCostKes = 100,
                servings = 10,
                imageResName = "mandazi",
                description = "Lightly sweet, pillowy fried dough — the classic breakfast side with chai."
            ),
            Recipe(
                name = "Kenyan Chai",
                mealType = MealType.BREAKFAST,
                prepTimeMinutes = 5,
                cookTimeMinutes = 10,
                difficulty = Difficulty.EASY,
                rating = 4.9f,
                totalCostKes = 40,
                servings = 4,
                imageResName = "kenyan_chai",
                description = "Milky, spiced tea boiled together rather than steeped — a daily ritual."
            ),
            Recipe(
                name = "Mukimo",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 20,
                cookTimeMinutes = 30,
                difficulty = Difficulty.EASY,
                rating = 4.6f,
                totalCostKes = 150,
                servings = 5,
                imageResName = "mukimo",
                description = "Mashed potatoes, maize, beans and greens — hearty and deeply comforting."
            ),
            Recipe(
                name = "Kachumbari",
                mealType = MealType.SNACK,
                prepTimeMinutes = 10,
                cookTimeMinutes = 0,
                difficulty = Difficulty.EASY,
                rating = 4.4f,
                totalCostKes = 50,
                servings = 4,
                imageResName = "kachumbari",
                description = "Fresh tomato-onion salad with a squeeze of lemon — the go-to side for grilled meat."

            ),
            Recipe(
                name = "Nyama Choma",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 15,
                cookTimeMinutes = 50,
                difficulty = Difficulty.MEDIUM,
                rating = 4.9f,
                totalCostKes = 600,
                servings = 4,
                imageResName = "nyama_choma",
                description = "Kenya's iconic grilled meat — charcoal-roasted goat or beef, smoky and simple."
            ),
            Recipe(
                name = "Wali wa Nazi",
                mealType = MealType.LUNCH,
                prepTimeMinutes = 10,
                cookTimeMinutes = 25,
                difficulty = Difficulty.EASY,
                rating = 4.6f,
                totalCostKes = 150,
                servings = 4,
                imageResName = "wali_wa_nazi",
                description = "Rice cooked in coconut milk — fragrant, slightly sweet, a coastal Kenyan favourite."
            ),
            Recipe(name = "Ndengu",
                mealType = MealType.LUNCH,
                prepTimeMinutes = 10,
                cookTimeMinutes = 40,
                difficulty = Difficulty.EASY,
                rating = 4.5f,
                totalCostKes = 120,
                servings = 4,
                imageResName = "ndegu",
                description = "Green gram stew simmered with onions and tomatoes — light, healthy, budget-friendly.")
            ,
            Recipe(name = "Uji",
                mealType = MealType.BREAKFAST,
                prepTimeMinutes = 5,
                cookTimeMinutes = 15,
                difficulty = Difficulty.EASY,
                rating = 4.5f,
                totalCostKes = 50,
                servings = 4,
                imageResName = "uji",
                description = "Warm fermented porridge — a gentle, comforting start to the day."
            ),
            Recipe(
                name = "Bhajia",
                mealType = MealType.SNACK,
                prepTimeMinutes = 15,
                cookTimeMinutes = 15,
                difficulty = Difficulty.MEDIUM, rating = 4.7f,
                totalCostKes = 150,
                servings = 4,
                imageResName = "bhajia",
                description = "Spiced potato slices in a crisp gram-flour batter — the ultimate tea-time snack."
            ),
            Recipe(
                name = "Samosa",
                mealType = MealType.SNACK,
                prepTimeMinutes = 30,
                cookTimeMinutes = 20,
                difficulty = Difficulty.MEDIUM,
                rating = 4.8f,
                totalCostKes = 120,
                servings = 8,
                imageResName = "samosa",
                description = "Crispy triangles filled with spiced minced beef and peas."
            ),
            Recipe(
                name = "Managu na Nyama",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 15,
                cookTimeMinutes = 35,
                difficulty = Difficulty.EASY,
                rating = 4.4f,
                totalCostKes = 200,
                servings = 4,
                imageResName = "managu_na_nyama",
                description = "African nightshade greens simmered with tender beef — earthy and nourishing."
            ),
            Recipe(
                name = "Omena kwa Nazi",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 10, cookTimeMinutes = 25,
                difficulty = Difficulty.MEDIUM,
                rating = 4.3f,
                totalCostKes = 120,
                servings = 4,
                imageResName = "omena",
                description = "Silver fish simmered in coconut sauce — a Lake Victoria classic."
            ),
            Recipe(
                name = "Matoke",
                mealType = MealType.LUNCH,
                prepTimeMinutes = 15,
                cookTimeMinutes = 35,
                difficulty = Difficulty.EASY,
                rating = 4.5f,
                totalCostKes = 150,
                servings = 4,
                imageResName = "matoke",
                description = "Green bananas simmered with tomatoes and onions into a soft, savoury stew."
            ),
            Recipe(
                name = "Kuku wa Kienyeji",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 20,
                cookTimeMinutes = 60,
                difficulty = Difficulty.MEDIUM,
                rating = 4.8f,
                totalCostKes = 200,
                servings = 4,
                imageResName = "kuku_ya_kienyeji",
                description = "Free-range chicken stewed slowly until tender — a treasured Sunday dish."
            ),
            Recipe(
                name = "Mchuzi wa Samaki",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 15,
                cookTimeMinutes = 30,
                difficulty = Difficulty.MEDIUM,
                rating = 4.6f,
                totalCostKes = 250,
                servings = 4,
                imageResName = "samaki",
                description = "Fish simmered in a spiced tomato sauce — a coastal favourite."
            ),
            Recipe(
                name = "Mchuzi wa Nyama",
                mealType = MealType.SUPPER,
                prepTimeMinutes = 15,
                cookTimeMinutes = 50,
                difficulty = Difficulty.EASY,
                rating = 4.7f,
                totalCostKes = 250,
                servings = 4,
                imageResName = "nyama",
                description = "Classic Kenyan beef stew, slow-cooked in a rich tomato and onion sauce."
            ),
            Recipe(name = "Kunde",
                mealType = MealType.LUNCH,
                prepTimeMinutes = 10,
                cookTimeMinutes = 25,
                difficulty = Difficulty.EASY,
                rating = 4.3f,
                totalCostKes = 80,
                servings = 4,
                imageResName = "kunde",
                description = "Cowpea leaves cooked down with onions and tomatoes — earthy and nutritious."
            ),
            Recipe(
                name = "Mahamri",
                mealType = MealType.BREAKFAST,
                prepTimeMinutes = 20,
                cookTimeMinutes = 15,
                difficulty = Difficulty.MEDIUM,
                rating = 4.7f,
                totalCostKes = 80,
                servings = 8,
                imageResName = "mahamri",
                description = "Coastal coconut-flavoured fried bread, lightly spiced with cardamom."
            ),
            Recipe(
                name = "Terere",
                mealType = MealType.LUNCH,
                prepTimeMinutes = 10,
                cookTimeMinutes = 20,
                difficulty = Difficulty.EASY,
                rating = 4.2f,
                totalCostKes = 100,
                servings = 4,
                imageResName = "terere",
                description = "Amaranth greens sautéed simply with onions and tomatoes."
            ),
            Recipe(name = "Mahindi Choma", mealType = MealType.SNACK, prepTimeMinutes = 5, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 30, servings = 2, imageResName = "mahindi_choma",
                description = "Roasted maize on the cob, charred over open flame and finished with lemon and salt."),
            Recipe(name = "Sukuma Wiki na Nyama", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 35,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 350, servings = 4, imageResName = "sukuma_wiki_na_nyama",
                description = "Kale sautéed together with tender beef — a heartier take on the everyday classic."),
            Recipe(name = "Kuku Kienyeji Fry", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 40,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 500, servings = 4, imageResName = "kuku_kienyeji_fry",
                description = "Free-range chicken dry-fried with garlic and ginger until deeply golden."),
            Recipe(name = "Mursik", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.3f, totalCostKes = 65, servings = 2, imageResName = "mursik",
                description = "Traditional fermented milk from the Rift Valley, smoky and tangy."),
            Recipe(name = "Kaimati", mealType = MealType.SNACK, prepTimeMinutes = 20, cookTimeMinutes = 15,
                difficulty = Difficulty.MEDIUM, rating = 4.6f, totalCostKes = 130, servings = 8, imageResName = "kaimati",
                description = "Sweet, cardamom-spiced fried dough balls from the Kenyan coast."),
            Recipe(name = "Mkate wa Sinia", mealType = MealType.BREAKFAST, prepTimeMinutes = 20, cookTimeMinutes = 30,
                difficulty = Difficulty.MEDIUM, rating = 4.5f, totalCostKes = 200, servings = 8, imageResName = "mkate_wa_sinia",
                description = "Coastal rice bread baked in a tray, lightly sweet and fragrant with coconut."),
            Recipe(name = "Kachori", mealType = MealType.SNACK, prepTimeMinutes = 20, cookTimeMinutes = 20,
                difficulty = Difficulty.MEDIUM, rating = 4.5f, totalCostKes = 140, servings = 6, imageResName = "kachori",
                description = "Crispy fried snack with a spiced lentil filling."),
            Recipe(name = "Mutura", mealType = MealType.SNACK, prepTimeMinutes = 20, cookTimeMinutes = 30,
                difficulty = Difficulty.MEDIUM, rating = 4.4f, totalCostKes = 300, servings = 4, imageResName = "mutura",
                description = "Kenyan-style grilled sausage, richly spiced — popular roadside fare."),
            Recipe(name = "Spaghetti Bolognese", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 35,
                difficulty = Difficulty.EASY, rating = 4.7f, totalCostKes = 450, servings = 4, imageResName = "spaghetti_bolognese",
                description = "Classic Italian-style pasta with a rich minced beef and tomato sauce."),
            Recipe(name = "Grilled Cheese Sandwich", mealType = MealType.SNACK, prepTimeMinutes = 5, cookTimeMinutes = 10,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 180, servings = 1, imageResName = "grilled_cheese_sandwich",
                description = "Buttery toasted bread with melted cheese — a quick comfort classic."),
            Recipe(name = "Vegetable Stir Fry", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 250, servings = 3, imageResName = "vegetable_stir_fry",
                description = "Crisp vegetables tossed quickly in soy sauce — light and colourful."),
            Recipe(name = "Chickpea Salad", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.3f, totalCostKes = 200, servings = 3, imageResName = "chickpen_salad",
                description = "Fresh, protein-packed salad with chickpeas, cucumber, and a lime dressing."),
            Recipe(name = "Lentil Soup", mealType = MealType.LUNCH, prepTimeMinutes = 10, cookTimeMinutes = 30,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 180, servings = 4, imageResName = "lentil_soup",
                description = "Warming, spiced lentil soup — simple and deeply nourishing."),
            Recipe(name = "Pork Chops with Herbs", mealType = MealType.SUPPER, prepTimeMinutes = 10, cookTimeMinutes = 25,
                difficulty = Difficulty.MEDIUM, rating = 4.6f, totalCostKes = 650, servings = 3, imageResName = "pork_chops_with_herbs",
                description = "Pan-seared pork chops finished with basil, oregano, and black pepper."),
            Recipe(name = "Banana Pancakes", mealType = MealType.BREAKFAST, prepTimeMinutes = 10, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.7f, totalCostKes = 150, servings = 3, imageResName = "banana_pancakes",
                description = "Soft, naturally sweet pancakes made with ripe bananas and a drizzle of honey."),
            Recipe(name = "Greek-Style Yogurt Bowl", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 200, servings = 1, imageResName = "greek_style_yogurt_bowl",
                description = "Creamy yogurt topped with honey and fresh apple slices — a bright, easy breakfast."),
            Recipe(name = "Irio", mealType = MealType.SUPPER, prepTimeMinutes = 20, cookTimeMinutes = 30,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 200, servings = 5, imageResName = "irio",
                description = "Mashed potatoes, peas, and maize — a close cousin of mukimo, smooth and comforting."),
            Recipe(name = "Sukuma na Maharagwe", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 30,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 180, servings = 4, imageResName = "sukuma_na_maharagwe",
                description = "Kale and kidney beans simmered together — a filling, everyday combination."),
            Recipe(name = "Kuku wa Nazi", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 45,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 550, servings = 4, imageResName = "kuku_wa_nazi",
                description = "Chicken simmered in a fragrant coconut sauce — a coastal favourite."),
            Recipe(name = "Samaki wa Kukaanga", mealType = MealType.SUPPER, prepTimeMinutes = 10, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 400, servings = 3, imageResName = "samaki_wa_kukaanga",
                description = "Whole fish marinated and deep-fried until crisp — simple and satisfying."),
            Recipe(name = "Chapati Mayai", mealType = MealType.SNACK, prepTimeMinutes = 20, cookTimeMinutes = 15,
                difficulty = Difficulty.MEDIUM, rating = 4.8f, totalCostKes = 150, servings = 2, imageResName = "chapati_mayai",
                description = "Chapati dough cooked together with beaten egg — Kenya's beloved street-food \"rolex\"."),
            Recipe(name = "Maharagwe ya Nazi", mealType = MealType.LUNCH, prepTimeMinutes = 10, cookTimeMinutes = 35,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 220, servings = 4, imageResName = "maharagwe_ya_nazi",
                description = "Kidney beans simmered in coconut milk until rich and creamy."),
            Recipe(name = "Mabuyu", mealType = MealType.SNACK, prepTimeMinutes = 10, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.2f, totalCostKes = 50, servings = 4, imageResName = "mabuyu",
                description = "Sweet-and-sour candied baobab seeds — a nostalgic Kenyan childhood snack."),
            Recipe(name = "Kachumbari ya Parachichi", mealType = MealType.SNACK, prepTimeMinutes = 10, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 90, servings = 4, imageResName = "kachumbari_ya_parachichi",
                description = "A creamy twist on kachumbari, with ripe avocado folded through."),
            Recipe(name = "Margherita Pizza", mealType = MealType.SUPPER, prepTimeMinutes = 30, cookTimeMinutes = 20,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 400, servings = 4, imageResName = "margherita_pizza",
                description = "Classic Italian pizza with tomato, mozzarella-style cheese, and fresh basil."),
            Recipe(name = "Caesar Salad", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 250, servings = 2, imageResName = "caesar_salad",
                description = "Crisp lettuce, toasted bread croutons, and cheese in a simple dressing."),
            Recipe(name = "Vegetable Fried Rice", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 300, servings = 4, imageResName = "vegetable_fried_rice",
                description = "Quick fried rice loaded with vegetables and a savoury soy glaze."),
            Recipe(name = "Chicken Curry", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 40,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 500, servings = 4, imageResName = "chicken_curry",
                description = "Chicken simmered in a spiced coconut curry sauce."),
            Recipe(name = "Classic Pancakes", mealType = MealType.BREAKFAST, prepTimeMinutes = 10, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 150, servings = 3, imageResName = "classic_pancakes",
                description = "Fluffy stacked pancakes, simple and reliable, finished with honey."),
            Recipe(name = "Cheese Omelette", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 10,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 180, servings = 1, imageResName = "cheese_omelette",
                description = "Fluffy eggs folded over melted cheese and bell pepper."),
            Recipe(name = "Beef Stir Fry", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 450, servings = 3, imageResName = "beef_stir_fry",
                description = "Thin-sliced beef seared quickly with peppers and broccoli in a savoury sauce."),
            Recipe(name = "Mac and Cheese", mealType = MealType.LUNCH, prepTimeMinutes = 10, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.7f, totalCostKes = 350, servings = 4, imageResName = "mac_and_cheese",
                description = "Creamy baked pasta smothered in melted cheese — pure comfort food."),
            Recipe(name = "Wet Fry", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 35,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 450, servings = 4, imageResName = "wet_fry",
                description = "Chicken pan-fried with onions and tomatoes in its own rich, saucy gravy — a Kenyan restaurant favourite."),
            Recipe(name = "Ugali wa Mtama", mealType = MealType.SUPPER, prepTimeMinutes = 10, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.3f, totalCostKes = 130, servings = 4, imageResName = "ugali_wa_mtama",
                description = "Millet ugali — denser and earthier than the maize version, a traditional favourite."),
            Recipe(name = "Muthokoi", mealType = MealType.LUNCH, prepTimeMinutes = 20, cookTimeMinutes = 90,
                difficulty = Difficulty.MEDIUM, rating = 4.4f, totalCostKes = 150, servings = 5, imageResName = "muthokoi",
                description = "Dehulled maize and beans boiled low and slow until soft — a filling Kamba classic."),
            Recipe(name = "Kimanga", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 30,
                difficulty = Difficulty.MEDIUM, rating = 4.5f, totalCostKes = 350, servings = 4, imageResName = "kimanga",
                description = "Coastal mash of ripe plantains with spiced meat stewed through."),
            Recipe(name = "Kunde na Nyama", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 30,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 280, servings = 4, imageResName = "kunde_na_nyama",
                description = "Cowpea leaves simmered together with beef — a close cousin of managu na nyama."),
            Recipe(name = "Ndizi Kaanga", mealType = MealType.SNACK, prepTimeMinutes = 10, cookTimeMinutes = 10,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 80, servings = 3, imageResName = "ndizi_kaanga",
                description = "Sliced ripe plantains fried until caramelised at the edges — a favourite quick snack."),
            Recipe(name = "Karanga", mealType = MealType.SNACK, prepTimeMinutes = 5, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.2f, totalCostKes = 60, servings = 4, imageResName = "karanga",
                description = "Simple roasted groundnuts, lightly salted — a classic snack to go with tea."),
            Recipe(name = "Ugali wa Muhogo", mealType = MealType.SUPPER, prepTimeMinutes = 10, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.2f, totalCostKes = 100, servings = 4, imageResName = "ugali_wa_muhogo",
                description = "Cassava-flour ugali, softer and slightly sweet compared to the maize version."),
            Recipe(name = "French Toast", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 10,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 150, servings = 2, imageResName = "french_toast",
                description = "Bread soaked in sweetened egg and pan-fried until golden."),
            Recipe(name = "Chickpea Dip", mealType = MealType.SNACK, prepTimeMinutes = 10, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.3f, totalCostKes = 200, servings = 4, imageResName = "chickpea_dip",
                description = "Smooth blended chickpeas with lime and garlic — a simple, healthy dip."),
            Recipe(name = "Banana Bread", mealType = MealType.BREAKFAST, prepTimeMinutes = 15, cookTimeMinutes = 45,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 250, servings = 8, imageResName = "banana_bread",
                description = "Moist, sweet loaf made with ripe mashed bananas."),
            Recipe(name = "Beef Wraps", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 400, servings = 3, imageResName = "beef_wraps",
                description = "Spiced beef and peppers rolled up in a soft chapati-style wrap."),
            Recipe(name = "Chicken Fried Rice", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 350, servings = 4, imageResName = "chicken_fried_rice",
                description = "Fried rice tossed with chicken, carrots, and egg in a savoury soy glaze."),
            Recipe(name = "Potato Wedges", mealType = MealType.SNACK, prepTimeMinutes = 10, cookTimeMinutes = 25,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 120, servings = 3, imageResName = "potato_wedges",
                description = "Crisp, paprika-seasoned potato wedges."),
            Recipe(name = "Tomato Soup", mealType = MealType.LUNCH, prepTimeMinutes = 10, cookTimeMinutes = 25,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 180, servings = 4, imageResName = "tomato_soup",
                description = "Smooth, comforting soup made from simmered tomatoes and a touch of milk."),
            Recipe(name = "Banana Smoothie", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 100, servings = 2, imageResName = "banana_smoothie",
                description = "Quick blended banana, milk, and honey — a fast, filling breakfast drink."),
            Recipe(name = "Matumbo", mealType = MealType.SUPPER, prepTimeMinutes = 20, cookTimeMinutes = 90,
                difficulty = Difficulty.MEDIUM, rating = 4.3f, totalCostKes = 350, servings = 4, imageResName = "matumbo",
                description = "Tripe stewed low and slow with onions and tomatoes until tender."),
            Recipe(name = "Mchicha na Nyama", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 30,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 300, servings = 4, imageResName = "mchicha_na_nyama",
                description = "Spinach simmered together with beef — simple, iron-rich, and hearty."),

            Recipe(name = "Uji wa Mtama", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 15,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 70, servings = 4, imageResName = "uji_wa_mtama",
                description = "Millet porridge — earthier and more textured than the usual maize uji."),
            Recipe(name = "Supu ya Kuku", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 45,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 300, servings = 4, imageResName = "supu_ya_kuku",
                description = "Simple, soothing chicken soup simmered with onions, carrots, and garlic."),
            Recipe(name = "Chai ya Tangawizi", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 10,
                difficulty = Difficulty.EASY, rating = 4.7f, totalCostKes = 40, servings = 4, imageResName = "chai_ya_tangawizi",
                description = "Kenyan tea boiled through with fresh ginger for extra warmth."),
            Recipe(name = "Maziwa Lala", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.2f, totalCostKes = 70, servings = 2, imageResName = "maziwa_lala",
                description = "Traditional fermented milk, left to sour naturally until thick and tangy."),
            Recipe(name = "Wali wa Kukaanga", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 25,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 280, servings = 4, imageResName = "wali_wa_kukaanga",
                description = "Spiced fried rice with caramelised onions, garlic, and cardamom."),
            Recipe(name = "Biryani", mealType = MealType.SUPPER, prepTimeMinutes = 25, cookTimeMinutes = 60,
                difficulty = Difficulty.MEDIUM, rating = 4.8f, totalCostKes = 550, servings = 5, imageResName = "biryani",
                description = "Fragrant layered chicken and rice, a Kenyan-coast favourite for special occasions."),
            Recipe(name = "Ugali na Mayai", mealType = MealType.SUPPER, prepTimeMinutes = 10, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 150, servings = 3, imageResName = "ugali_na_mayai",
                description = "Ugali served alongside a simple fried-egg omelette — quick and filling."),
            Recipe(name = "Kuku Choma", mealType = MealType.SUPPER, prepTimeMinutes = 20, cookTimeMinutes = 40,
                difficulty = Difficulty.MEDIUM, rating = 4.8f, totalCostKes = 500, servings = 4, imageResName = "kuku_choma",
                description = "Charcoal-grilled chicken, marinated in lemon and salt until smoky and tender."),
            Recipe(name = "Pilau ya Samaki", mealType = MealType.SUPPER, prepTimeMinutes = 20, cookTimeMinutes = 40,
                difficulty = Difficulty.MEDIUM, rating = 4.6f, totalCostKes = 450, servings = 4, imageResName = "pilau_ya_samaki",
                description = "Fragrant spiced rice folded gently with pan-fried fish."),
            Recipe(name = "Mishkaki", mealType = MealType.SNACK, prepTimeMinutes = 30, cookTimeMinutes = 20,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 350, servings = 4, imageResName = "mishkaki",
                description = "Marinated beef skewers grilled over charcoal — a popular street-side favourite."),
            Recipe(name = "Ndizi na Nazi", mealType = MealType.SNACK, prepTimeMinutes = 10, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 150, servings = 4, imageResName = "ndizi_na_nazi",
                description = "Ripe plantains simmered gently in sweetened coconut milk until soft."),
            Recipe(name = "Beef Tacos", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 400, servings = 4, imageResName = "beef_tacos",
                description = "Spiced beef and peppers folded into soft homemade tortillas."),
            Recipe(name = "Vegetable Curry", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 30,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 300, servings = 4, imageResName = "vegetable_curry",
                description = "Potatoes, carrots, and peas simmered in a gentle coconut curry sauce."),
            Recipe(name = "Fruit Salad", mealType = MealType.SNACK, prepTimeMinutes = 15, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 250, servings = 4, imageResName = "fruit_salad",
                description = "A refreshing mix of mango, pineapple, orange, and watermelon."),
            Recipe(name = "Chocolate Cake", mealType = MealType.SNACK, prepTimeMinutes = 20, cookTimeMinutes = 40,
                difficulty = Difficulty.MEDIUM, rating = 4.8f, totalCostKes = 450, servings = 8, imageResName = "chocolate_cake",
                description = "Rich, moist chocolate cake — a proper treat for any occasion."),
            Recipe(name = "Shrimp Pasta", mealType = MealType.SUPPER, prepTimeMinutes = 15, cookTimeMinutes = 20,
                difficulty = Difficulty.MEDIUM, rating = 4.7f, totalCostKes = 550, servings = 3, imageResName = "shrimp_pasta",
                description = "Garlicky prawns tossed with pasta and a bright squeeze of lemon."),
            Recipe(name = "Roast Chicken", mealType = MealType.SUPPER, prepTimeMinutes = 20, cookTimeMinutes = 75,
                difficulty = Difficulty.MEDIUM, rating = 4.8f, totalCostKes = 600, servings = 4, imageResName = "roast_chicken",
                description = "Whole chicken roasted with butter, garlic, and oregano until golden."),
            Recipe(name = "Coleslaw", mealType = MealType.SNACK, prepTimeMinutes = 15, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.3f, totalCostKes = 150, servings = 4, imageResName = "coleslaw",
                description = "Crisp shredded cabbage and carrot in a creamy yogurt dressing."),
            Recipe(name = "Pumpkin Soup", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 30,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 200, servings = 4, imageResName = "pumpkin_soup",
                description = "Smooth, velvety pumpkin soup finished with a touch of milk."),
            Recipe(name = "Sweet Potato Mash", mealType = MealType.LUNCH, prepTimeMinutes = 15, cookTimeMinutes = 25,
                difficulty = Difficulty.EASY, rating = 4.4f, totalCostKes = 130, servings = 4, imageResName = "sweet_potato_mash",
                description = "Creamy mashed sweet potatoes with butter and a splash of milk."),
            Recipe(name = "Beetroot Salad", mealType = MealType.SNACK, prepTimeMinutes = 15, cookTimeMinutes = 20,
                difficulty = Difficulty.EASY, rating = 4.2f, totalCostKes = 120, servings = 3, imageResName = "beetroot_salad",
                description = "Earthy boiled beetroot tossed with thin onion slices and lime."),
            Recipe(name = "Hot Dog", mealType = MealType.SNACK, prepTimeMinutes = 5, cookTimeMinutes = 10,
                difficulty = Difficulty.EASY, rating = 4.3f, totalCostKes = 200, servings = 2, imageResName = "hot_dog",
                description = "Grilled sausages tucked into soft bread — quick and satisfying."),
            Recipe(name = "Almond Granola", mealType = MealType.BREAKFAST, prepTimeMinutes = 10, cookTimeMinutes = 25,
                difficulty = Difficulty.EASY, rating = 4.5f, totalCostKes = 300, servings = 6, imageResName = "almond_granola" ,
                description = "Crunchy baked oats and almonds sweetened with honey."),
            Recipe(name = "Stuffed Bell Peppers", mealType = MealType.SUPPER, prepTimeMinutes = 20, cookTimeMinutes = 35,
                difficulty = Difficulty.MEDIUM, rating = 4.6f, totalCostKes = 350, servings = 4, imageResName = "stuffed_bell_peppers",
                description = "Bell peppers filled with spiced beef and rice, topped with melted cheese."),
            Recipe(name = "Mango Smoothie", mealType = MealType.BREAKFAST, prepTimeMinutes = 5, cookTimeMinutes = 0,
                difficulty = Difficulty.EASY, rating = 4.6f, totalCostKes = 120, servings = 2, imageResName = "mango_smoothie",
                description = "Sweet, creamy blended mango with milk and honey.")

        )
        val recipeIds = db.recipeDao().insertRecipes(recipes)
        val recipeIdOf = recipes.mapIndexed { i, r -> r.name to recipeIds[i] }.toMap()

        // ---- 3. Ingredients per recipe ----
        fun ri(
            recipeName: String, ingredientName: String, qty: String, unit: String?,
            standardizedAmount: Double? = null, standardizedUnit: String? = null
        ) =
            RecipeIngredient(
                recipeId = recipeIdOf.getValue(recipeName),
                ingredientId = idOf.getValue(ingredientName),
                quantity = qty,
                unit = unit,
                standardizedAmount = standardizedAmount,
                standardizedUnit = standardizedUnit
            )

        val recipeIngredients = listOf(
            // Ugali na Sukuma Wiki
            ri("Ugali na Sukuma Wiki", "Maize flour (unga)", "3", "cups", 375.0, "g"),
            ri("Ugali na Sukuma Wiki", "Sukuma wiki (kale)", "1", "bunch", 1.0, "bunch"),
            ri("Ugali na Sukuma Wiki", "Onions", "1", "piece", 1.0, "piece"),
            ri("Ugali na Sukuma Wiki", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Ugali na Sukuma Wiki", "Cooking oil", "2", "tbsp", 30.0, "ml"),
            ri("Ugali na Sukuma Wiki", "Salt", "1", "tsp", 5.0, "g"),

            // Chapati
            ri("Chapati", "Wheat flour", "3", "cups", 375.0, "g"),
            ri("Chapati", "Cooking oil", "4", "tbsp", 60.0, "ml"),
            ri("Chapati", "Salt", "1", "tsp", 5.0, "g"),

            // Githeri
            ri("Githeri", "Green maize (fresh)", "3", "cups"),
            ri("Githeri", "Kidney beans", "2", "cups", 340.0, "g"),
            ri("Githeri", "Onions", "1", "piece", 1.0, "piece"),
            ri("Githeri", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Githeri", "Carrots", "1", "piece", 1.0, "piece"),
            ri("Githeri", "Cooking oil", "2", "tbsp", 30.0, "ml"),
            ri("Githeri", "Royco / beef masala", "1", "cube", 1.0, "cube"),

            // Pilau
            ri("Pilau", "Rice", "3", "cups", 600.0, "g"),
            ri("Pilau", "Beef", "500", "g", 500.0, "g"),
            ri("Pilau", "Onions", "2", "pieces", 2.0, "piece"),
            ri("Pilau", "Garlic", "3", "cloves"),
            ri("Pilau", "Ginger", "1", "tbsp"),
            ri("Pilau", "Cardamom", "4", "pods"),
            ri("Pilau", "Cinnamon", "1", "stick", 1.0, "stick"),
            ri("Pilau", "Cooking oil", "3", "tbsp", 45.0, "ml"),

            // Mandazi
            ri("Mandazi", "Wheat flour", "4", "cups", 500.0, "g"),
            ri("Mandazi", "Sugar", "0.5", "cup", 100.0, "g"),
            ri("Mandazi", "Milk", "1", "cup", 250.0, "ml"),
            ri("Mandazi", "Baking powder", "1", "tbsp", 15.0, "g"),
            ri("Mandazi", "Cardamom", "3", "pods"),
            ri("Mandazi", "Cooking oil", "3", "cups", 720.0, "ml"),

            // Kenyan Chai
            ri("Kenyan Chai", "Milk", "2", "cups", 500.0, "ml"),
            ri("Kenyan Chai", "Tea leaves", "2", "tbsp", 10.0, "g"),
            ri("Kenyan Chai", "Sugar", "3", "tbsp", 37.5, "g"),
            ri("Kenyan Chai", "Ginger", "1", "tsp"),

            // Mukimo
            ri("Mukimo", "Potatoes", "5", "pieces", 5.0, "piece"),
            ri("Mukimo", "Green maize (fresh)", "1", "cup"),
            ri("Mukimo", "Kidney beans", "1", "cup", 170.0, "g"),
            ri("Mukimo", "Sukuma wiki (kale)", "0.5", "bunch", 0.5, "bunch"),
            ri("Mukimo", "Onions", "1", "piece", 1.0, "piece"),

            // Kachumbari
            ri("Kachumbari", "Tomatoes", "3", "pieces", 3.0, "piece"),
            ri("Kachumbari", "Onions", "1", "piece", 1.0, "piece"),
            ri("Kachumbari", "Coriander (dhania)", "1", "handful", 1.0, "handful"),
            ri("Kachumbari", "Lemon", "1", "piece", 1.0, "piece"),

            ri("Nyama Choma", "Goat meat", "1", "kg", 1000.0, "g"),
            ri("Nyama Choma", "Salt", "1", "tsp", 5.0, "g"),
            ri("Nyama Choma", "Lemon", "1", "piece", 1.0, "piece"),

            ri("Wali wa Nazi", "Rice", "2", "cups", 400.0, "g"),
            ri("Wali wa Nazi", "Coconut milk", "2", "cups", 480.0, "ml"),
            ri("Wali wa Nazi", "Onions", "1", "piece", 1.0, "piece"),
            ri("Wali wa Nazi", "Cooking oil", "1", "tbsp", 15.0, "ml"),
            ri("Wali wa Nazi", "Salt", "1", "tsp", 5.0, "g"),

            ri("Ndengu", "Green grams (ndengu)", "1.5", "cups", 285.0, "g"),
            ri("Ndengu", "Onions", "1", "piece", 1.0, "piece"),
            ri("Ndengu", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Ndengu", "Cooking oil", "2", "tbsp", 30.0, "ml"),
            ri("Ndengu", "Royco / beef masala", "1", "cube", 1.0, "cube"),

            ri("Uji", "Maize flour (unga)", "1", "cup", 125.0, "g"),
            ri("Uji", "Milk", "1", "cup", 250.0, "ml"),
            ri("Uji", "Sugar", "2", "tbsp", 25.0, "g"),

            ri("Bhajia", "Potatoes", "4", "pieces", 4.0, "piece"),
            ri("Bhajia", "Gram flour (besan)", "1", "cup", 100.0, "g"),
            ri("Bhajia", "Curry powder", "1", "tsp", 5.0, "g"),
            ri("Bhajia", "Cooking oil", "2", "cups", 480.0, "ml"),
            ri("Bhajia", "Salt", "1", "tsp", 5.0, "g"),

            ri("Samosa", "Wheat flour", "2", "cups", 250.0, "g"),
            ri("Samosa", "Beef", "250", "g", 250.0, "g"),
            ri("Samosa", "Peas", "0.5", "cup", 75.0, "g"),
            ri("Samosa", "Onions", "1", "piece", 1.0, "piece"),
            ri("Samosa", "Curry powder", "1", "tsp", 5.0, "g"),
            ri("Samosa", "Cooking oil", "2", "cups", 480.0, "ml"),

            ri("Managu na Nyama", "Managu (African nightshade)", "1", "bunch", 1.0, "bunch"),
            ri("Managu na Nyama", "Beef", "300", "g", 300.0, "g"),
            ri("Managu na Nyama", "Onions", "1", "piece", 1.0, "piece"),
            ri("Managu na Nyama", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Managu na Nyama", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Omena kwa Nazi", "Omena (dried fish)", "1", "cup", 150.0, "g"),
            ri("Omena kwa Nazi", "Coconut milk", "1.5", "cups", 360.0, "ml"),
            ri("Omena kwa Nazi", "Onions", "1", "piece", 1.0, "piece"),
            ri("Omena kwa Nazi", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Omena kwa Nazi", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Matoke", "Green bananas (matoke)", "8", "pieces", 8.0, "piece"),
            ri("Matoke", "Onions", "1", "piece", 1.0, "piece"),
            ri("Matoke", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Matoke", "Cooking oil", "2", "tbsp", 30.0, "ml"),
            ri("Matoke", "Royco / beef masala", "1", "cube", 1.0, "cube"),

            ri("Kuku wa Kienyeji", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Kuku wa Kienyeji", "Onions", "2", "pieces", 2.0, "piece"),
            ri("Kuku wa Kienyeji", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Kuku wa Kienyeji", "Garlic", "3", "cloves"),
            ri("Kuku wa Kienyeji", "Ginger", "1", "tbsp"),
            ri("Kuku wa Kienyeji", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Mchuzi wa Samaki", "Fish (tilapia)", "1", "whole", 1.0, "piece"),
            ri("Mchuzi wa Samaki", "Onions", "1", "piece", 1.0, "piece"),
            ri("Mchuzi wa Samaki", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Mchuzi wa Samaki", "Coconut milk", "1", "cup", 240.0, "ml"),
            ri("Mchuzi wa Samaki", "Curry powder", "1", "tsp", 5.0, "g"),
            ri("Mchuzi wa Samaki", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Mchuzi wa Nyama", "Beef", "500", "g", 500.0, "g"),
            ri("Mchuzi wa Nyama", "Onions", "2", "pieces", 2.0, "piece"),
            ri("Mchuzi wa Nyama", "Tomatoes", "3", "pieces", 3.0, "piece"),
            ri("Mchuzi wa Nyama", "Garlic", "2", "cloves"),
            ri("Mchuzi wa Nyama", "Cooking oil", "2", "tbsp", 30.0, "ml"),
            ri("Mchuzi wa Nyama", "Royco / beef masala", "1", "cube", 1.0, "cube"),

            ri("Kunde", "Cowpea leaves (kunde)", "1", "bunch", 1.0, "bunch"),
            ri("Kunde", "Onions", "1", "piece", 1.0, "piece"),
            ri("Kunde", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Kunde", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Mahamri", "Wheat flour", "3", "cups", 375.0, "g"),
            ri("Mahamri", "Coconut milk", "1", "cup", 240.0, "ml"),
            ri("Mahamri", "Sugar", "3", "tbsp", 37.5, "g"),
            ri("Mahamri", "Yeast", "1", "tsp", 4.0, "g"),
            ri("Mahamri", "Cardamom", "3", "pods"),
            ri("Mahamri", "Cooking oil", "3", "cups", 720.0, "ml"),

            ri("Terere", "Terere (amaranth greens)", "1", "bunch", 1.0, "bunch"),
            ri("Terere", "Onions", "1", "piece", 1.0, "piece"),
            ri("Terere", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Terere", "Cooking oil", "1", "tbsp", 15.0, "ml"),

            ri("Mahindi Choma", "Green maize (fresh)", "2", "cobs"),
            ri("Mahindi Choma", "Lemon", "1", "piece", 1.0, "piece"),
            ri("Mahindi Choma", "Salt", "1", "tsp", 5.0, "g"),

            ri("Sukuma Wiki na Nyama", "Sukuma wiki (kale)", "1", "bunch", 1.0, "bunch"),
            ri("Sukuma Wiki na Nyama", "Beef", "300", "g", 300.0, "g"),
            ri("Sukuma Wiki na Nyama", "Onions", "1", "piece", 1.0, "piece"),
            ri("Sukuma Wiki na Nyama", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Sukuma Wiki na Nyama", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Kuku Kienyeji Fry", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Kuku Kienyeji Fry", "Garlic", "3", "cloves"),
            ri("Kuku Kienyeji Fry", "Ginger", "1", "tbsp"),
            ri("Kuku Kienyeji Fry", "Lemon", "1", "piece", 1.0, "piece"),
            ri("Kuku Kienyeji Fry", "Cooking oil", "3", "tbsp", 45.0, "ml"),

            ri("Mursik", "Milk", "2", "cups", 500.0, "ml"),

            ri("Kaimati", "Wheat flour", "2", "cups", 250.0, "g"),
            ri("Kaimati", "Sugar", "2", "tbsp", 25.0, "g"),
            ri("Kaimati", "Yeast", "1", "tsp", 4.0, "g"),
            ri("Kaimati", "Cardamom", "2", "pods"),
            ri("Kaimati", "Cooking oil", "2", "cups", 480.0, "ml"),

            ri("Mkate wa Sinia", "Rice", "2", "cups", 400.0, "g"),
            ri("Mkate wa Sinia", "Coconut milk", "1", "cup", 240.0, "ml"),
            ri("Mkate wa Sinia", "Sugar", "0.5", "cup", 100.0, "g"),
            ri("Mkate wa Sinia", "Yeast", "1", "tsp", 4.0, "g"),

            ri("Kachori", "Green grams (ndengu)", "1", "cup", 190.0, "g"),
            ri("Kachori", "Gram flour (besan)", "0.5", "cup", 50.0, "g"),
            ri("Kachori", "Cumin", "1", "tsp", 5.0, "g"),
            ri("Kachori", "Curry powder", "1", "tsp", 5.0, "g"),
            ri("Kachori", "Cooking oil", "2", "cups", 480.0, "ml"),

            ri("Mutura", "Goat meat", "500", "g", 500.0, "g"),
            ri("Mutura", "Onions", "1", "piece", 1.0, "piece"),
            ri("Mutura", "Curry powder", "1", "tsp", 5.0, "g"),
            ri("Mutura", "Salt", "1", "tsp", 5.0, "g"),

            ri("Spaghetti Bolognese", "Pasta", "300", "g", 300.0, "g"),
            ri("Spaghetti Bolognese", "Beef", "400", "g", 400.0, "g"),
            ri("Spaghetti Bolognese", "Onions", "1", "piece", 1.0, "piece"),
            ri("Spaghetti Bolognese", "Tomatoes", "3", "pieces", 3.0, "piece"),
            ri("Spaghetti Bolognese", "Oregano", "1", "tsp", 2.0, "g"),
            ri("Spaghetti Bolognese", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Grilled Cheese Sandwich", "Bread", "2", "slices"),
            ri("Grilled Cheese Sandwich", "Cheese", "2", "slices", 40.0, "g"),
            ri("Grilled Cheese Sandwich", "Butter", "1", "tbsp", 15.0, "g"),

            ri("Vegetable Stir Fry", "Bell pepper", "1", "piece", 1.0, "piece"),
            ri("Vegetable Stir Fry", "Broccoli", "1", "piece", 1.0, "piece"),
            ri("Vegetable Stir Fry", "Mushrooms", "1", "cup", 100.0, "g"),
            ri("Vegetable Stir Fry", "Soy sauce", "2", "tbsp", 30.0, "ml"),
            ri("Vegetable Stir Fry", "Cooking oil", "1", "tbsp", 15.0, "ml"),

            ri("Chickpea Salad", "Chickpeas", "1.5", "cups", 250.0, "g"),
            ri("Chickpea Salad", "Cucumber", "1", "piece", 1.0, "piece"),
            ri("Chickpea Salad", "Lettuce", "1", "piece", 1.0, "piece"),
            ri("Chickpea Salad", "Lime", "1", "piece", 1.0, "piece"),

            ri("Lentil Soup", "Lentils", "1.5", "cups", 300.0, "g"),
            ri("Lentil Soup", "Onions", "1", "piece", 1.0, "piece"),
            ri("Lentil Soup", "Cumin", "1", "tsp", 5.0, "g"),
            ri("Lentil Soup", "Paprika", "1", "tsp", 5.0, "g"),
            ri("Lentil Soup", "Cooking oil", "1", "tbsp", 15.0, "ml"),

            ri("Pork Chops with Herbs", "Pork", "500", "g", 500.0, "g"),
            ri("Pork Chops with Herbs", "Basil", "1", "tsp", 2.0, "g"),
            ri("Pork Chops with Herbs", "Oregano", "1", "tsp", 2.0, "g"),
            ri("Pork Chops with Herbs", "Black pepper", "1", "tsp", 5.0, "g"),
            ri("Pork Chops with Herbs", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Banana Pancakes", "Bananas", "2", "pieces", 2.0, "piece"),
            ri("Banana Pancakes", "Wheat flour", "1", "cup", 125.0, "g"),
            ri("Banana Pancakes", "Milk", "0.5", "cup", 125.0, "ml"),
            ri("Banana Pancakes", "Honey", "1", "tbsp", 20.0, "ml"),

            ri("Greek-Style Yogurt Bowl", "Yogurt", "1", "cup", 250.0, "ml"),
            ri("Greek-Style Yogurt Bowl", "Honey", "1", "tbsp", 20.0, "ml"),
            ri("Greek-Style Yogurt Bowl", "Apples", "1", "piece", 1.0, "piece"),

            ri("Irio", "Potatoes", "5", "pieces", 5.0, "piece"),
            ri("Irio", "Peas", "1", "cup", 150.0, "g"),
            ri("Irio", "Green maize (fresh)", "1", "cup"),
            ri("Irio", "Onions", "1", "piece", 1.0, "piece"),

            ri("Sukuma na Maharagwe", "Sukuma wiki (kale)", "1", "bunch", 1.0, "bunch"),
            ri("Sukuma na Maharagwe", "Kidney beans", "1.5", "cups", 255.0, "g"),
            ri("Sukuma na Maharagwe", "Onions", "1", "piece", 1.0, "piece"),
            ri("Sukuma na Maharagwe", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Kuku wa Nazi", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Kuku wa Nazi", "Coconut milk", "1.5", "cups", 360.0, "ml"),
            ri("Kuku wa Nazi", "Onions", "1", "piece", 1.0, "piece"),
            ri("Kuku wa Nazi", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Kuku wa Nazi", "Curry powder", "1", "tsp", 5.0, "g"),

            ri("Samaki wa Kukaanga", "Fish (tilapia)", "1", "whole", 1.0, "piece"),
            ri("Samaki wa Kukaanga", "Lemon", "1", "piece", 1.0, "piece"),
            ri("Samaki wa Kukaanga", "Salt", "1", "tsp", 5.0, "g"),
            ri("Samaki wa Kukaanga", "Cooking oil", "2", "cups", 480.0, "ml"),

            ri("Chapati Mayai", "Wheat flour", "1.5", "cups", 190.0, "g"),
            ri("Chapati Mayai", "Eggs", "2", "pieces", 2.0, "piece"),
            ri("Chapati Mayai", "Onions", "1", "piece", 1.0, "piece"),
            ri("Chapati Mayai", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Maharagwe ya Nazi", "Kidney beans", "2", "cups", 340.0, "g"),
            ri("Maharagwe ya Nazi", "Coconut milk", "1.5", "cups", 360.0, "ml"),
            ri("Maharagwe ya Nazi", "Onions", "1", "piece", 1.0, "piece"),
            ri("Maharagwe ya Nazi", "Tomatoes", "2", "pieces", 2.0, "piece"),

            ri("Mabuyu", "Baobab fruit (mabuyu)", "1", "cup", 200.0, "g"),
            ri("Mabuyu", "Sugar", "3", "tbsp", 37.5, "g"),

            ri("Kachumbari ya Parachichi", "Avocado", "1", "piece", 1.0, "piece"),
            ri("Kachumbari ya Parachichi", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Kachumbari ya Parachichi", "Onions", "1", "piece", 1.0, "piece"),
            ri("Kachumbari ya Parachichi", "Lemon", "1", "piece", 1.0, "piece"),

            ri("Margherita Pizza", "Wheat flour", "2", "cups", 250.0, "g"),
            ri("Margherita Pizza", "Yeast", "1", "tsp", 4.0, "g"),
            ri("Margherita Pizza", "Tomatoes", "3", "pieces", 3.0, "piece"),
            ri("Margherita Pizza", "Cheese", "150", "g", 150.0, "g"),
            ri("Margherita Pizza", "Basil", "1", "tsp", 2.0, "g"),

            ri("Caesar Salad", "Lettuce", "1", "piece", 1.0, "piece"),
            ri("Caesar Salad", "Bread", "2", "slices"),
            ri("Caesar Salad", "Cheese", "50", "g", 50.0, "g"),
            ri("Caesar Salad", "Black pepper", "1", "tsp", 5.0, "g"),

            ri("Vegetable Fried Rice", "Rice", "2", "cups", 400.0, "g"),
            ri("Vegetable Fried Rice", "Eggs", "2", "pieces", 2.0, "piece"),
            ri("Vegetable Fried Rice", "Carrots", "1", "piece", 1.0, "piece"),
            ri("Vegetable Fried Rice", "Peas", "0.5", "cup", 75.0, "g"),
            ri("Vegetable Fried Rice", "Soy sauce", "2", "tbsp", 30.0, "ml"),

            ri("Chicken Curry", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Chicken Curry", "Coconut milk", "1.5", "cups", 360.0, "ml"),
            ri("Chicken Curry", "Onions", "1", "piece", 1.0, "piece"),
            ri("Chicken Curry", "Curry powder", "2", "tsp", 10.0, "g"),

            ri("Classic Pancakes", "Wheat flour", "1.5", "cups", 190.0, "g"),
            ri("Classic Pancakes", "Eggs", "2", "pieces", 2.0, "piece"),
            ri("Classic Pancakes", "Milk", "1", "cup", 250.0, "ml"),
            ri("Classic Pancakes", "Honey", "1", "tbsp", 20.0, "ml"),

            ri("Cheese Omelette", "Eggs", "3", "pieces", 3.0, "piece"),
            ri("Cheese Omelette", "Cheese", "40", "g", 40.0, "g"),
            ri("Cheese Omelette", "Bell pepper", "0.5", "piece", 0.5, "piece"),

            ri("Beef Stir Fry", "Beef", "400", "g", 400.0, "g"),
            ri("Beef Stir Fry", "Bell pepper", "1", "piece", 1.0, "piece"),
            ri("Beef Stir Fry", "Broccoli", "1", "piece", 1.0, "piece"),
            ri("Beef Stir Fry", "Soy sauce", "2", "tbsp", 30.0, "ml"),

            ri("Mac and Cheese", "Pasta", "300", "g", 300.0, "g"),
            ri("Mac and Cheese", "Cheese", "200", "g", 200.0, "g"),
            ri("Mac and Cheese", "Butter", "2", "tbsp", 30.0, "g"),
            ri("Mac and Cheese", "Milk", "1", "cup", 250.0, "ml"),

            ri("Wet Fry", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Wet Fry", "Onions", "2", "pieces", 2.0, "piece"),
            ri("Wet Fry", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Wet Fry", "Garlic", "2", "cloves"),
            ri("Wet Fry", "Cooking oil", "3", "tbsp", 45.0, "ml"),

            ri("Ugali wa Mtama", "Millet flour", "3", "cups", 375.0, "g"),
            ri("Ugali wa Mtama", "Salt", "1", "tsp", 5.0, "g"),

            ri("Muthokoi", "Dried maize (muthokoi)", "2", "cups", 320.0, "g"),
            ri("Muthokoi", "Kidney beans", "1", "cup", 170.0, "g"),
            ri("Muthokoi", "Onions", "1", "piece", 1.0, "piece"),
            ri("Muthokoi", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Kimanga", "Ripe plantains", "4", "pieces", 4.0, "piece"),
            ri("Kimanga", "Beef", "300", "g", 300.0, "g"),
            ri("Kimanga", "Onions", "1", "piece", 1.0, "piece"),
            ri("Kimanga", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Kimanga", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Kunde na Nyama", "Cowpea leaves (kunde)", "1", "bunch", 1.0, "bunch"),
            ri("Kunde na Nyama", "Beef", "300", "g", 300.0, "g"),
            ri("Kunde na Nyama", "Onions", "1", "piece", 1.0, "piece"),
            ri("Kunde na Nyama", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Kunde na Nyama", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Ndizi Kaanga", "Ripe plantains", "3", "pieces", 3.0, "piece"),
            ri("Ndizi Kaanga", "Cooking oil", "1", "cup", 240.0, "ml"),

            ri("Karanga", "Groundnuts (njugu)", "2", "cups", 300.0, "g"),
            ri("Karanga", "Salt", "1", "tsp", 5.0, "g"),

            ri("Ugali wa Muhogo", "Cassava flour", "3", "cups", 375.0, "g"),
            ri("Ugali wa Muhogo", "Salt", "1", "tsp", 5.0, "g"),

            ri("French Toast", "Bread", "4", "slices"),
            ri("French Toast", "Eggs", "2", "pieces", 2.0, "piece"),
            ri("French Toast", "Milk", "0.5", "cup", 125.0, "ml"),
            ri("French Toast", "Sugar", "1", "tbsp", 12.5, "g"),
            ri("French Toast", "Butter", "1", "tbsp", 15.0, "g"),

            ri("Chickpea Dip", "Chickpeas", "2", "cups", 340.0, "g"),
            ri("Chickpea Dip", "Lime", "1", "piece", 1.0, "piece"),
            ri("Chickpea Dip", "Garlic", "2", "cloves"),
            ri("Chickpea Dip", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Banana Bread", "Bananas", "4", "pieces", 4.0, "piece"),
            ri("Banana Bread", "Wheat flour", "2", "cups", 250.0, "g"),
            ri("Banana Bread", "Sugar", "0.75", "cup", 150.0, "g"),
            ri("Banana Bread", "Eggs", "2", "pieces", 2.0, "piece"),
            ri("Banana Bread", "Butter", "100", "g", 100.0, "g"),
            ri("Banana Bread", "Baking powder", "1", "tbsp", 15.0, "g"),

            ri("Beef Wraps", "Beef", "400", "g", 400.0, "g"),
            ri("Beef Wraps", "Wheat flour", "2", "cups", 250.0, "g"),
            ri("Beef Wraps", "Onions", "1", "piece", 1.0, "piece"),
            ri("Beef Wraps", "Bell pepper", "1", "piece", 1.0, "piece"),
            ri("Beef Wraps", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Chicken Fried Rice", "Rice", "2", "cups", 400.0, "g"),
            ri("Chicken Fried Rice", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Chicken Fried Rice", "Eggs", "2", "pieces", 2.0, "piece"),
            ri("Chicken Fried Rice", "Carrots", "1", "piece", 1.0, "piece"),
            ri("Chicken Fried Rice", "Soy sauce", "2", "tbsp", 30.0, "ml"),

            ri("Potato Wedges", "Potatoes", "4", "pieces", 4.0, "piece"),
            ri("Potato Wedges", "Paprika", "1", "tsp", 5.0, "g"),
            ri("Potato Wedges", "Cooking oil", "2", "tbsp", 30.0, "ml"),
            ri("Potato Wedges", "Salt", "1", "tsp", 5.0, "g"),

            ri("Tomato Soup", "Tomatoes", "6", "pieces", 6.0, "piece"),
            ri("Tomato Soup", "Onions", "1", "piece", 1.0, "piece"),
            ri("Tomato Soup", "Milk", "1", "cup", 250.0, "ml"),
            ri("Tomato Soup", "Butter", "1", "tbsp", 15.0, "g"),

            ri("Banana Smoothie", "Bananas", "2", "pieces", 2.0, "piece"),
            ri("Banana Smoothie", "Milk", "1.5", "cups", 375.0, "ml"),
            ri("Banana Smoothie", "Honey", "1", "tbsp", 20.0, "ml"),

            ri("Matumbo", "Tripe (matumbo)", "1", "kg", 1000.0, "g"),
            ri("Matumbo", "Onions", "1", "piece", 1.0, "piece"),
            ri("Matumbo", "Tomatoes", "2", "pieces", 2.0, "piece"),
            ri("Matumbo", "Royco / beef masala", "1", "cube", 1.0, "cube"),
            ri("Matumbo", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Mchicha na Nyama", "Spinach", "1", "bunch", 1.0, "bunch"),
            ri("Mchicha na Nyama", "Beef", "300", "g", 300.0, "g"),
            ri("Mchicha na Nyama", "Onions", "1", "piece", 1.0, "piece"),
            ri("Mchicha na Nyama", "Tomatoes", "1", "piece", 1.0, "piece"),
            ri("Mchicha na Nyama", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Uji wa Mtama", "Millet flour", "1", "cup", 125.0, "g"),
            ri("Uji wa Mtama", "Milk", "1", "cup", 250.0, "ml"),
            ri("Uji wa Mtama", "Sugar", "2", "tbsp", 25.0, "g"),

            ri("Supu ya Kuku", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Supu ya Kuku", "Onions", "1", "piece", 1.0, "piece"),
            ri("Supu ya Kuku", "Carrots", "1", "piece", 1.0, "piece"),
            ri("Supu ya Kuku", "Garlic", "2", "cloves"),
            ri("Supu ya Kuku", "Salt", "1", "tsp", 5.0, "g"),

            ri("Chai ya Tangawizi", "Milk", "2", "cups", 500.0, "ml"),
            ri("Chai ya Tangawizi", "Tea leaves", "2", "tbsp", 10.0, "g"),
            ri("Chai ya Tangawizi", "Ginger", "1", "tbsp"),
            ri("Chai ya Tangawizi", "Sugar", "3", "tbsp", 37.5, "g"),

            ri("Maziwa Lala", "Milk", "2", "cups", 500.0, "ml"),

            ri("Wali wa Kukaanga", "Rice", "2", "cups", 400.0, "g"),
            ri("Wali wa Kukaanga", "Onions", "1", "piece", 1.0, "piece"),
            ri("Wali wa Kukaanga", "Garlic", "2", "cloves"),
            ri("Wali wa Kukaanga", "Cardamom", "3", "pods"),
            ri("Wali wa Kukaanga", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Biryani", "Rice", "3", "cups", 600.0, "g"),
            ri("Biryani", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Biryani", "Onions", "2", "pieces", 2.0, "piece"),
            ri("Biryani", "Garlic", "3", "cloves"),
            ri("Biryani", "Ginger", "1", "tbsp"),
            ri("Biryani", "Curry powder", "2", "tsp", 10.0, "g"),
            ri("Biryani", "Cooking oil", "3", "tbsp", 45.0, "ml"),

            ri("Ugali na Mayai", "Maize flour (unga)", "3", "cups", 375.0, "g"),
            ri("Ugali na Mayai", "Eggs", "3", "pieces", 3.0, "piece"),
            ri("Ugali na Mayai", "Onions", "1", "piece", 1.0, "piece"),
            ri("Ugali na Mayai", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Kuku Choma", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Kuku Choma", "Lemon", "1", "piece", 1.0, "piece"),
            ri("Kuku Choma", "Salt", "1", "tsp", 5.0, "g"),
            ri("Kuku Choma", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Pilau ya Samaki", "Rice", "3", "cups", 600.0, "g"),
            ri("Pilau ya Samaki", "Fish (tilapia)", "1", "whole", 1.0, "piece"),
            ri("Pilau ya Samaki", "Onions", "2", "pieces", 2.0, "piece"),
            ri("Pilau ya Samaki", "Cardamom", "3", "pods"),
            ri("Pilau ya Samaki", "Cinnamon", "1", "stick", 1.0, "stick"),
            ri("Pilau ya Samaki", "Cooking oil", "3", "tbsp", 45.0, "ml"),

            ri("Mishkaki", "Beef", "500", "g", 500.0, "g"),
            ri("Mishkaki", "Lemon", "1", "piece", 1.0, "piece"),
            ri("Mishkaki", "Garlic", "2", "cloves"),
            ri("Mishkaki", "Curry powder", "1", "tsp", 5.0, "g"),
            ri("Mishkaki", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Ndizi na Nazi", "Ripe plantains", "4", "pieces", 4.0, "piece"),
            ri("Ndizi na Nazi", "Coconut milk", "1", "cup", 240.0, "ml"),
            ri("Ndizi na Nazi", "Sugar", "2", "tbsp", 25.0, "g"),

            ri("Beef Tacos", "Beef", "400", "g", 400.0, "g"),
            ri("Beef Tacos", "Wheat flour", "2", "cups", 250.0, "g"),
            ri("Beef Tacos", "Onions", "1", "piece", 1.0, "piece"),
            ri("Beef Tacos", "Bell pepper", "1", "piece", 1.0, "piece"),
            ri("Beef Tacos", "Chili peppers", "1", "piece"),
            ri("Beef Tacos", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Vegetable Curry", "Potatoes", "3", "pieces", 3.0, "piece"),
            ri("Vegetable Curry", "Carrots", "1", "piece", 1.0, "piece"),
            ri("Vegetable Curry", "Peas", "1", "cup", 150.0, "g"),
            ri("Vegetable Curry", "Onions", "1", "piece", 1.0, "piece"),
            ri("Vegetable Curry", "Coconut milk", "1", "cup", 240.0, "ml"),
            ri("Vegetable Curry", "Curry powder", "2", "tsp", 10.0, "g"),
            ri("Vegetable Curry", "Cooking oil", "2", "tbsp", 30.0, "ml"),

            ri("Fruit Salad", "Mango", "2", "pieces", 2.0, "piece"),
            ri("Fruit Salad", "Pineapple", "1", "piece", 1.0, "piece"),
            ri("Fruit Salad", "Orange", "2", "pieces", 2.0, "piece"),
            ri("Fruit Salad", "Watermelon", "1", "slice", 1.0, "piece"),

            ri("Chocolate Cake", "Wheat flour", "2", "cups", 250.0, "g"),
            ri("Chocolate Cake", "Cocoa powder", "0.5", "cup", 50.0, "g"),
            ri("Chocolate Cake", "Sugar", "1", "cup", 200.0, "g"),
            ri("Chocolate Cake", "Eggs", "3", "pieces", 3.0, "piece"),
            ri("Chocolate Cake", "Butter", "100", "g", 100.0, "g"),
            ri("Chocolate Cake", "Vanilla essence", "1", "tsp", 5.0, "ml"),
            ri("Chocolate Cake", "Baking powder", "1", "tbsp", 15.0, "g"),

            ri("Shrimp Pasta", "Pasta", "300", "g", 300.0, "g"),
            ri("Shrimp Pasta", "Prawns", "300", "g", 300.0, "g"),
            ri("Shrimp Pasta", "Garlic", "2", "cloves"),
            ri("Shrimp Pasta", "Butter", "2", "tbsp", 30.0, "g"),
            ri("Shrimp Pasta", "Lemon", "1", "piece", 1.0, "piece"),

            ri("Roast Chicken", "Chicken", "1", "whole", 1.0, "piece"),
            ri("Roast Chicken", "Butter", "50", "g", 50.0, "g"),
            ri("Roast Chicken", "Garlic", "3", "cloves"),
            ri("Roast Chicken", "Oregano", "1", "tsp", 2.0, "g"),
            ri("Roast Chicken", "Salt", "1", "tsp", 5.0, "g"),

            ri("Coleslaw", "Cabbage", "1", "piece", 1.0, "piece"),
            ri("Coleslaw", "Carrots", "1", "piece", 1.0, "piece"),
            ri("Coleslaw", "Yogurt", "0.5", "cup", 125.0, "ml"),

            ri("Pumpkin Soup", "Pumpkin", "1", "piece", 1.0, "piece"),
            ri("Pumpkin Soup", "Onions", "1", "piece", 1.0, "piece"),
            ri("Pumpkin Soup", "Milk", "1", "cup", 250.0, "ml"),
            ri("Pumpkin Soup", "Butter", "1", "tbsp", 15.0, "g"),

            ri("Sweet Potato Mash", "Sweet potatoes", "4", "pieces", 4.0, "piece"),
            ri("Sweet Potato Mash", "Butter", "30", "g", 30.0, "g"),
            ri("Sweet Potato Mash", "Milk", "0.5", "cup", 125.0, "ml"),

            ri("Beetroot Salad", "Beetroot", "3", "pieces", 3.0, "piece"),
            ri("Beetroot Salad", "Lime", "1", "piece", 1.0, "piece"),
            ri("Beetroot Salad", "Onions", "0.5", "piece", 0.5, "piece"),

            ri("Hot Dog", "Sausages", "4", "pieces", 320.0, "g"),
            ri("Hot Dog", "Bread", "2", "slices"),

            ri("Almond Granola", "Oats", "2", "cups", 160.0, "g"),
            ri("Almond Granola", "Honey", "3", "tbsp", 45.0, "ml"),
            ri("Almond Granola", "Almonds", "0.5", "cup", 60.0, "g"),

            ri("Stuffed Bell Peppers", "Bell pepper", "4", "pieces", 4.0, "piece"),
            ri("Stuffed Bell Peppers", "Rice", "1", "cup", 200.0, "g"),
            ri("Stuffed Bell Peppers", "Beef", "300", "g", 300.0, "g"),
            ri("Stuffed Bell Peppers", "Onions", "1", "piece", 1.0, "piece"),
            ri("Stuffed Bell Peppers", "Cheese", "100", "g", 100.0, "g"),

            ri("Mango Smoothie", "Mango", "2", "pieces", 2.0, "piece"),
            ri("Mango Smoothie", "Milk", "1", "cup", 250.0, "ml"),
            ri("Mango Smoothie", "Honey", "1", "tbsp", 20.0, "ml"),
        )
        db.recipeDao().insertRecipeIngredients(recipeIngredients)

        // ---- 4. Steps (with timers where cooking/waiting is involved) ----
        fun step(recipeName: String, num: Int, text: String, timerSec: Int? = null) =
            RecipeStep(
                recipeId = recipeIdOf.getValue(recipeName),
                stepNumber = num,
                instruction = text,
                timerSeconds = timerSec
            )

        val steps = listOf(
            // Ugali na Sukuma Wiki
            step("Ugali na Sukuma Wiki", 1, "Boil 4 cups of water in a heavy pot."),
            step(
                "Ugali na Sukuma Wiki",
                2,
                "Add maize flour gradually while stirring to avoid lumps.",
                timerSec = 300
            ),
            step(
                "Ugali na Sukuma Wiki",
                3,
                "Reduce heat and keep stirring until firm and smooth.",
                timerSec = 300
            ),
            step(
                "Ugali na Sukuma Wiki",
                4,
                "In a separate pan, sauté onions and tomatoes in oil until soft.",
                timerSec = 240
            ),
            step(
                "Ugali na Sukuma Wiki",
                5,
                "Add chopped sukuma wiki, season with salt, and cook until tender.",
                timerSec = 300
            ),
            step("Ugali na Sukuma Wiki", 6, "Serve the ugali hot alongside the sukuma wiki."),

            // Chapati
            step(
                "Chapati",
                1,
                "Mix flour, salt, and a little oil, then add warm water gradually to form a soft dough."
            ),
            step("Chapati", 2, "Knead the dough for 8–10 minutes until smooth.", timerSec = 600),
            step(
                "Chapati",
                3,
                "Cover and rest the dough for at least 20 minutes.",
                timerSec = 1200
            ),
            step(
                "Chapati",
                4,
                "Divide into balls, roll each flat, brush with oil, and roll up into a coil before rolling flat again — this gives the layers."
            ),
            step(
                "Chapati",
                5,
                "Cook each chapati on a hot dry pan, flipping until golden brown spots appear on both sides.",
                timerSec = 180
            ),

            // Githeri
            step(
                "Githeri",
                1,
                "Boil maize and beans together until soft (use pre-soaked beans to speed this up).",
                timerSec = 2400
            ),
            step(
                "Githeri",
                2,
                "In a separate pan, sauté onions until translucent.",
                timerSec = 180
            ),
            step(
                "Githeri",
                3,
                "Add tomatoes and cook until they break down into a sauce.",
                timerSec = 300
            ),
            step(
                "Githeri",
                4,
                "Add the boiled maize and beans along with diced carrots, stir in the seasoning cube."
            ),
            step(
                "Githeri",
                5,
                "Simmer everything together until the flavours combine.",
                timerSec = 600
            ),

            // Pilau
            step("Pilau", 1, "Brown the beef in oil until seared on all sides.", timerSec = 480),
            step(
                "Pilau",
                2,
                "Add onions and cook until deeply caramelized — this is what gives pilau its colour.",
                timerSec = 600
            ),
            step(
                "Pilau",
                3,
                "Add garlic, ginger, cardamom, and cinnamon, and fry until fragrant.",
                timerSec = 120
            ),
            step("Pilau", 4, "Add washed rice and enough water/stock, then bring to a boil."),
            step(
                "Pilau",
                5,
                "Cover and simmer on low heat until the rice is fully cooked.",
                timerSec = 1200
            ),

            // Mandazi
            step("Mandazi", 1, "Mix flour, sugar, baking powder, and cardamom in a bowl."),
            step(
                "Mandazi",
                2,
                "Add warm milk gradually and knead into a soft, slightly sticky dough.",
                timerSec = 300
            ),
            step("Mandazi", 3, "Cover and let the dough rest.", timerSec = 900),
            step("Mandazi", 4, "Roll out and cut into triangles or squares."),
            step(
                "Mandazi",
                5,
                "Deep fry in hot oil until golden brown on both sides.",
                timerSec = 240
            ),

            // Kenyan Chai
            step("Kenyan Chai", 1, "Bring water to a boil with grated ginger.", timerSec = 180),
            step("Kenyan Chai", 2, "Add tea leaves and let them boil for a minute.", timerSec = 60),
            step(
                "Kenyan Chai",
                3,
                "Pour in the milk and sugar, then bring back to a boil.",
                timerSec = 240
            ),
            step(
                "Kenyan Chai",
                4,
                "Simmer until the chai reaches your preferred strength and colour.",
                timerSec = 180
            ),

            // Mukimo
            step(
                "Mukimo",
                1,
                "Boil potatoes, maize, and beans together until soft.",
                timerSec = 1500
            ),
            step(
                "Mukimo",
                2,
                "In a separate pot, boil the sukuma wiki until tender.",
                timerSec = 300
            ),
            step(
                "Mukimo",
                3,
                "Drain and mash everything together into a smooth, green-flecked mash."
            ),
            step("Mukimo", 4, "Sauté onions separately and stir through, or serve alongside."),

            // Kachumbari
            step(
                "Kachumbari",
                1,
                "Finely dice the tomatoes and onions."),

            step(
                "Kachumbari",
                2,
                "Chop the coriander and mix everything together."),

            step(
                "Kachumbari", 3, "Squeeze fresh lemon juice over the top and season to taste."),

            step("Nyama Choma", 1, "Season the meat generously with salt and let it marinate.", timerSec = 900),
            step("Nyama Choma", 2, "Light the charcoal grill and let it heat until glowing.", timerSec = 600),
            step("Nyama Choma", 3, "Grill the meat, turning occasionally, until well done throughout.", timerSec = 2400),
            step("Nyama Choma", 4, "Rest briefly, squeeze fresh lemon over it, and serve with kachumbari."),

            step("Wali wa Nazi", 1, "Sauté the onions in oil until soft.", timerSec = 180),
            step("Wali wa Nazi", 2, "Add the rice and stir to coat it in the oil."),
            step("Wali wa Nazi", 3, "Pour in the coconut milk and a little water, then bring to a boil."),
            step("Wali wa Nazi", 4, "Cover and simmer on low heat until the rice is fully cooked and fluffy.", timerSec = 1200),

            step("Ndengu", 1, "Boil the green grams until soft.", timerSec = 1800),
            step("Ndengu", 2, "In a separate pan, sauté onions and tomatoes until it forms a sauce.", timerSec = 300),
            step("Ndengu", 3, "Add the boiled green grams and seasoning cube, then simmer to combine flavours.", timerSec = 600),

            step("Uji", 1, "Mix the maize flour with a little cold water into a smooth paste."),
            step("Uji", 2, "Bring water to a boil, then stir in the paste continuously to avoid lumps.", timerSec = 600),
            step("Uji", 3, "Stir in the milk and sugar, then simmer gently.", timerSec = 300),

            step("Bhajia", 1, "Slice the potatoes thinly."),
            step("Bhajia", 2, "Mix gram flour, curry powder, salt, and water into a smooth batter."),
            step("Bhajia", 3, "Dip potato slices into the batter and deep fry until golden and crisp.", timerSec = 180),

            step("Samosa", 1, "Make a dough with flour, water, and a little oil, then rest it.", timerSec = 900),
            step("Samosa", 2, "Cook minced beef with onions, peas, and curry powder to make the filling.", timerSec = 900),
            step("Samosa", 3, "Roll the dough thin, cut into strips, fill, and fold into triangles."),
            step("Samosa", 4, "Deep fry until golden brown on all sides.", timerSec = 300),

            step("Managu na Nyama", 1, "Brown the beef until tender.", timerSec = 1500),
            step("Managu na Nyama", 2, "Sauté onions and tomatoes until soft.", timerSec = 240),
            step("Managu na Nyama", 3, "Add the cooked beef and chopped managu, then simmer until the greens are tender.", timerSec = 600),

            step("Omena kwa Nazi", 1, "Rinse and briefly soak the omena."),
            step("Omena kwa Nazi", 2, "Sauté onions and tomatoes until soft.", timerSec = 240),
            step("Omena kwa Nazi", 3, "Add the omena and cook briefly.", timerSec = 300),
            step("Omena kwa Nazi", 4, "Pour in the coconut milk and simmer until the sauce thickens.", timerSec = 900),

            step("Matoke", 1, "Peel and slice the green bananas."),
            step("Matoke", 2, "Sauté onions and tomatoes until soft.", timerSec = 240),
            step("Matoke", 3, "Add the bananas and enough water to cover, then simmer until tender.", timerSec = 1500),
            step("Matoke", 4, "Stir in the seasoning cube and mash lightly to thicken."),

            step("Kuku wa Kienyeji", 1, "Clean and cut the chicken into pieces."),
            step("Kuku wa Kienyeji", 2, "Sauté onions, garlic, and ginger until fragrant.", timerSec = 180),
            step("Kuku wa Kienyeji", 3, "Add the chicken and tomatoes, browning well.", timerSec = 600),
            step("Kuku wa Kienyeji", 4, "Add water and simmer slowly until the chicken is tender.", timerSec = 2700),

            step("Mchuzi wa Samaki", 1, "Clean the fish and lightly fry it until firm.", timerSec = 420),
            step("Mchuzi wa Samaki", 2, "Sauté onions and tomatoes into a thick sauce.", timerSec = 300),
            step("Mchuzi wa Samaki", 3, "Add curry powder and coconut milk, then simmer.", timerSec = 180),
            step("Mchuzi wa Samaki", 4, "Gently add the fish back in and simmer until cooked through.", timerSec = 600),

            step("Mchuzi wa Nyama", 1, "Brown the beef in oil until seared.", timerSec = 480),
            step("Mchuzi wa Nyama", 2, "Add onions, garlic, and tomatoes, and cook down into a sauce.", timerSec = 360),
            step("Mchuzi wa Nyama", 3, "Add water and the seasoning cube, then simmer until the beef is tender.", timerSec = 2400),

            step("Kunde", 1, "Wash and roughly chop the cowpea leaves."),
            step("Kunde", 2, "Sauté onions and tomatoes until soft.", timerSec = 240),
            step("Kunde", 3, "Add the cowpea leaves and simmer until tender.", timerSec = 600),

            step("Mahamri", 1, "Mix flour, sugar, yeast, and cardamom in a bowl."),
            step("Mahamri", 2, "Add warm coconut milk and knead into a soft dough.", timerSec = 300),
            step("Mahamri", 3, "Cover and let the dough rise.", timerSec = 2700),
            step("Mahamri", 4, "Roll out, cut into triangles, and deep fry until golden brown.", timerSec = 240),

            step("Terere", 1, "Wash and chop the amaranth greens."),
            step("Terere", 2, "Sauté onions and tomatoes until soft.", timerSec = 240),
            step("Terere", 3, "Add the greens and cook briefly until wilted.", timerSec = 300),

            step("Mahindi Choma", 1, "Roast the maize cobs directly over charcoal or open flame, turning often.", timerSec = 600),
            step("Mahindi Choma", 2, "Rub with lemon and a pinch of salt before serving."),

            step("Sukuma Wiki na Nyama", 1, "Brown the beef in oil until seared.", timerSec = 480),
            step("Sukuma Wiki na Nyama", 2, "Add onions and tomatoes, cooking until softened.", timerSec = 300),
            step("Sukuma Wiki na Nyama", 3, "Add chopped kale and simmer until tender.", timerSec = 480),

            step("Kuku Kienyeji Fry", 1, "Rub the chicken pieces with garlic, ginger, and lemon."),
            step("Kuku Kienyeji Fry", 2, "Fry in oil over medium heat, turning until deeply golden.", timerSec = 1800),

            step("Mursik", 1, "Pour the fermented milk into a traditional gourd or jug and stir well."),
            step("Mursik", 2, "Serve chilled."),

            step("Kaimati", 1, "Mix flour, sugar, yeast, and cardamom with warm water into a thick batter."),
            step("Kaimati", 2, "Cover and let rise until bubbly.", timerSec = 1800),
            step("Kaimati", 3, "Drop spoonfuls into hot oil and fry until golden brown balls form.", timerSec = 300),

            step("Mkate wa Sinia", 1, "Soak and grind the rice into a smooth batter."),
            step("Mkate wa Sinia", 2, "Mix in coconut milk, sugar, and yeast, then let it rise.", timerSec = 2400),
            step("Mkate wa Sinia", 3, "Pour into a tray and bake until golden.", timerSec = 1800),

            step("Kachori", 1, "Boil the green grams until soft, then mash with cumin and curry powder.", timerSec = 1500),
            step("Kachori", 2, "Make a dough with gram flour and water, then shape around the filling."),
            step("Kachori", 3, "Deep fry until crisp and golden.", timerSec = 300),

            step("Mutura", 1, "Season the minced goat meat with onions, curry powder, and salt."),
            step("Mutura", 2, "Stuff into casing and grill slowly over charcoal, turning often.", timerSec = 1500),

            step("Spaghetti Bolognese", 1, "Boil the pasta until al dente.", timerSec = 600),
            step("Spaghetti Bolognese", 2, "Brown the beef with onions until cooked through.", timerSec = 480),
            step("Spaghetti Bolognese", 3, "Add tomatoes and oregano, and simmer into a rich sauce.", timerSec = 900),
            step("Spaghetti Bolognese", 4, "Toss the sauce with the drained pasta and serve."),

            step("Grilled Cheese Sandwich", 1, "Butter one side of each bread slice."),
            step("Grilled Cheese Sandwich", 2, "Place cheese between the slices, buttered sides out."),
            step("Grilled Cheese Sandwich", 3, "Grill in a pan until golden and the cheese melts.", timerSec = 240),

            step("Vegetable Stir Fry", 1, "Slice all vegetables evenly."),
            step("Vegetable Stir Fry", 2, "Stir fry in hot oil over high heat for a few minutes.", timerSec = 300),
            step("Vegetable Stir Fry", 3, "Add soy sauce and toss to coat before serving."),

            step("Chickpea Salad", 1, "Drain and rinse the chickpeas."),
            step("Chickpea Salad", 2, "Dice the cucumber and shred the lettuce."),
            step("Chickpea Salad", 3, "Toss everything together with a squeeze of lime."),

            step("Lentil Soup", 1, "Sauté onions with cumin and paprika until fragrant.", timerSec = 180),
            step("Lentil Soup", 2, "Add lentils and enough water, then simmer until soft.", timerSec = 1500),
            step("Lentil Soup", 3, "Blend partially or leave chunky, and season to taste."),

            step("Pork Chops with Herbs", 1, "Season the pork chops with basil, oregano, and black pepper."),
            step("Pork Chops with Herbs", 2, "Sear in hot oil until golden on both sides and cooked through.", timerSec = 900),

            step("Banana Pancakes", 1, "Mash the bananas and mix with flour and milk into a batter."),
            step("Banana Pancakes", 2, "Cook spoonfuls on a hot pan until bubbles form, then flip.", timerSec = 180),
            step("Banana Pancakes", 3, "Drizzle with honey before serving."),

            step("Greek-Style Yogurt Bowl", 1, "Spoon the yogurt into a bowl."),
            step("Greek-Style Yogurt Bowl", 2, "Top with sliced apple and a drizzle of honey."),

            step("Irio", 1, "Boil potatoes, peas, and maize together until soft.", timerSec = 1500),
            step("Irio", 2, "Drain and mash together into a smooth, pale-green mash."),
            step("Irio", 3, "Sauté onions separately and stir through, or serve on the side."),

            step("Sukuma na Maharagwe", 1, "Boil the kidney beans until tender.", timerSec = 2400),
            step("Sukuma na Maharagwe", 2, "Sauté onions in oil until soft.", timerSec = 180),
            step("Sukuma na Maharagwe", 3, "Add chopped kale and the cooked beans, and simmer together.", timerSec = 480),

            step("Kuku wa Nazi", 1, "Brown the chicken pieces lightly in a pot.", timerSec = 480),
            step("Kuku wa Nazi", 2, "Add onions, tomatoes, and curry powder, cooking until fragrant.", timerSec = 300),
            step("Kuku wa Nazi", 3, "Pour in coconut milk and simmer until the chicken is tender.", timerSec = 1500),

            step("Samaki wa Kukaanga", 1, "Clean the fish and score the skin, then rub with lemon and salt."),
            step("Samaki wa Kukaanga", 2, "Deep fry until golden and crisp on both sides.", timerSec = 600),

            step("Chapati Mayai", 1, "Prepare a soft chapati dough and roll it out thin."),
            step("Chapati Mayai", 2, "Beat eggs with chopped onions."),
            step("Chapati Mayai", 3, "Cook the chapati partway on a hot pan, then pour the egg mixture over it and fold together as it cooks.", timerSec = 300),

            step("Maharagwe ya Nazi", 1, "Boil the kidney beans until soft.", timerSec = 2400),
            step("Maharagwe ya Nazi", 2, "Sauté onions and tomatoes until saucy.", timerSec = 300),
            step("Maharagwe ya Nazi", 3, "Add the beans and coconut milk, then simmer until creamy.", timerSec = 600),

            step("Mabuyu", 1, "Coat the baobab seeds evenly in sugar."),
            step("Mabuyu", 2, "Let sit until the sugar coating sets before serving."),

            step("Kachumbari ya Parachichi", 1, "Dice the tomatoes, onions, and avocado."),
            step("Kachumbari ya Parachichi", 2, "Gently toss together with a squeeze of lemon, being careful not to mash the avocado."),

            step("Margherita Pizza", 1, "Mix flour, yeast, and water into a dough and let it rise.", timerSec = 2700),
            step("Margherita Pizza", 2, "Roll out the dough and top with crushed tomatoes and cheese."),
            step("Margherita Pizza", 3, "Bake until the crust is golden and the cheese is bubbling.", timerSec = 900),
            step("Margherita Pizza", 4, "Scatter fresh basil over the top before serving."),

            step("Caesar Salad", 1, "Tear the lettuce into a bowl."),
            step("Caesar Salad", 2, "Toast the bread and cut into small croutons."),
            step("Caesar Salad", 3, "Toss with shaved cheese and a generous crack of black pepper."),

            step("Vegetable Fried Rice", 1, "Scramble the eggs in a hot pan and set aside."),
            step("Vegetable Fried Rice", 2, "Stir fry the carrots and peas briefly.", timerSec = 240),
            step("Vegetable Fried Rice", 3, "Add the cooked rice and soy sauce, tossing well, then fold the eggs back in.", timerSec = 300),

            step("Chicken Curry", 1, "Sauté onions until soft.", timerSec = 180),
            step("Chicken Curry", 2, "Add curry powder and chicken, browning lightly.", timerSec = 480),
            step("Chicken Curry", 3, "Pour in coconut milk and simmer until the chicken is cooked through.", timerSec = 1500),

            step("Classic Pancakes", 1, "Whisk flour, eggs, and milk into a smooth batter."),
            step("Classic Pancakes", 2, "Cook spoonfuls on a hot pan until bubbles form, then flip.", timerSec = 180),
            step("Classic Pancakes", 3, "Stack and drizzle with honey."),

            step("Cheese Omelette", 1, "Beat the eggs well."),
            step("Cheese Omelette", 2, "Pour into a hot pan and cook until nearly set.", timerSec = 180),
            step("Cheese Omelette", 3, "Add cheese and bell pepper, fold over, and cook briefly more.", timerSec = 60),

            step("Beef Stir Fry", 1, "Slice the beef thinly against the grain."),
            step("Beef Stir Fry", 2, "Sear the beef quickly in a very hot pan.", timerSec = 180),
            step("Beef Stir Fry", 3, "Add peppers and broccoli, stir frying briefly, then add soy sauce.", timerSec = 240),

            step("Mac and Cheese", 1, "Boil the pasta until al dente.", timerSec = 600),
            step("Mac and Cheese", 2, "Melt butter and stir in milk and cheese until smooth.", timerSec = 300),
            step("Mac and Cheese", 3, "Combine with the drained pasta and serve warm."),

            step("Wet Fry", 1, "Brown the chicken pieces in oil until golden.", timerSec = 600),
            step("Wet Fry", 2, "Add onions, tomatoes, and garlic, cooking down into a rich sauce.", timerSec = 480),
            step("Wet Fry", 3, "Simmer the chicken in the sauce until tender and well coated.", timerSec = 900),

            step("Ugali wa Mtama", 1, "Boil water in a heavy pot."),
            step("Ugali wa Mtama", 2, "Stir in millet flour gradually, avoiding lumps.", timerSec = 300),
            step("Ugali wa Mtama", 3, "Keep stirring until firm and smooth.", timerSec = 300),

            step("Muthokoi", 1, "Soak the maize and beans overnight before cooking."),
            step("Muthokoi", 2, "Boil together until very soft, topping up water as needed.", timerSec = 5400),
            step("Muthokoi", 3, "Sauté onions separately and stir through before serving.", timerSec = 240),

            step("Kimanga", 1, "Boil the plantains until soft.", timerSec = 900),
            step("Kimanga", 2, "Brown the beef with onions and tomatoes.", timerSec = 600),
            step("Kimanga", 3, "Mash the plantains and mix thoroughly with the cooked meat."),

            step("Kunde na Nyama", 1, "Brown the beef until seared.", timerSec = 480),
            step("Kunde na Nyama", 2, "Add onions and tomatoes, cooking until soft.", timerSec = 240),
            step("Kunde na Nyama", 3, "Add chopped kunde leaves and simmer until tender.", timerSec = 420),

            step("Ndizi Kaanga", 1, "Slice the plantains diagonally."),
            step("Ndizi Kaanga", 2, "Fry in hot oil until golden on both sides.", timerSec = 300),

            step("Karanga", 1, "Roast the groundnuts in a dry pan, stirring constantly.", timerSec = 600),
            step("Karanga", 2, "Sprinkle with salt while still hot."),

            step("Ugali wa Muhogo", 1, "Boil water in a heavy pot."),
            step("Ugali wa Muhogo", 2, "Stir in cassava flour gradually.", timerSec = 300),
            step("Ugali wa Muhogo", 3, "Cook until smooth and firm.", timerSec = 300),

            step("French Toast", 1, "Whisk eggs, milk, and sugar together."),
            step("French Toast", 2, "Dip bread slices in the mixture, coating both sides."),
            step("French Toast", 3, "Fry in butter until golden on both sides.", timerSec = 300),

            step("Chickpea Dip", 1, "Drain and rinse the chickpeas."),
            step("Chickpea Dip", 2, "Blend with lime juice, garlic, and oil until smooth."),
            step("Chickpea Dip", 3, "Season to taste and serve."),

            step("Banana Bread", 1, "Mash the bananas until smooth."),
            step("Banana Bread", 2, "Mix in sugar, eggs, and melted butter."),
            step("Banana Bread", 3, "Fold in flour and baking powder until just combined."),
            step("Banana Bread", 4, "Pour into a pan and bake until a skewer comes out clean.", timerSec = 2700),

            step("Beef Wraps", 1, "Prepare soft chapati-style wraps and cook on a dry pan.", timerSec = 600),
            step("Beef Wraps", 2, "Stir fry the beef with onions and peppers.", timerSec = 600),
            step("Beef Wraps", 3, "Fill the wraps with the beef mixture and roll up."),

            step("Chicken Fried Rice", 1, "Cook the rice and let it cool."),
            step("Chicken Fried Rice", 2, "Scramble the eggs and set aside."),
            step("Chicken Fried Rice", 3, "Stir fry the chicken and carrots until cooked through.", timerSec = 480),
            step("Chicken Fried Rice", 4, "Add the rice, soy sauce, chicken and eggs, tossing well.", timerSec = 300),

            step("Potato Wedges", 1, "Cut the potatoes into wedges."),
            step("Potato Wedges", 2, "Toss with oil, paprika, and salt."),
            step("Potato Wedges", 3, "Bake or fry until crisp and golden.", timerSec = 1200),

            step("Tomato Soup", 1, "Sauté onions in butter until soft.", timerSec = 240),
            step("Tomato Soup", 2, "Add tomatoes and simmer until soft.", timerSec = 900),
            step("Tomato Soup", 3, "Blend until smooth and stir in the milk."),

            step("Banana Smoothie", 1, "Blend bananas, milk, and honey until smooth."),
            step("Banana Smoothie", 2, "Serve chilled."),

            step("Matumbo", 1, "Clean the tripe thoroughly and cut into pieces."),
            step("Matumbo", 2, "Boil until tender, changing the water once if needed.", timerSec = 4500),
            step("Matumbo", 3, "Sauté onions and tomatoes, add the tripe and seasoning cube, and simmer together.", timerSec = 600),

            step("Mchicha na Nyama", 1, "Brown the beef until seared.", timerSec = 480),
            step("Mchicha na Nyama", 2, "Add onions and tomatoes, cooking until soft.", timerSec = 240),
            step("Mchicha na Nyama", 3, "Add chopped spinach and simmer briefly until wilted.", timerSec = 300),

            step("Uji wa Mtama", 1, "Mix millet flour with a little cold water into a smooth paste."),
            step("Uji wa Mtama", 2, "Bring water to a boil and stir in the paste, avoiding lumps.", timerSec = 480),
            step("Uji wa Mtama", 3, "Add milk and sugar, then simmer gently.", timerSec = 300),

            step("Supu ya Kuku", 1, "Place chicken, onions, carrots, and garlic in a pot with water."),
            step("Supu ya Kuku", 2, "Simmer until the chicken is tender and the broth is flavourful.", timerSec = 2700),
            step("Supu ya Kuku", 3, "Season with salt and serve hot."),

            step("Chai ya Tangawizi", 1, "Boil water with grated ginger.", timerSec = 180),
            step("Chai ya Tangawizi", 2, "Add tea leaves and boil briefly.", timerSec = 60),
            step("Chai ya Tangawizi", 3, "Add milk and sugar, then bring back to a boil.", timerSec = 240),

            step("Maziwa Lala", 1, "Pour fresh milk into a clean container and cover loosely."),
            step("Maziwa Lala", 2, "Leave at room temperature until it thickens and turns tangy, then chill before serving."),

            step("Wali wa Kukaanga", 1, "Fry onions and garlic in oil until golden.", timerSec = 300),
            step("Wali wa Kukaanga", 2, "Add cardamom and rice, stirring to coat."),
            step("Wali wa Kukaanga", 3, "Add water and simmer, covered, until the rice is fully cooked.", timerSec = 1200),

            step("Biryani", 1, "Marinate the chicken with garlic, ginger, and curry powder.", timerSec = 900),
            step("Biryani", 2, "Fry the onions slowly until deeply golden and set some aside for garnish.", timerSec = 900),
            step("Biryani", 3, "Brown the marinated chicken in the same pot.", timerSec = 600),
            step("Biryani", 4, "Layer with partially cooked rice, cover, and steam until the rice is fully done.", timerSec = 1500),

            step("Ugali na Mayai", 1, "Cook the ugali as usual — boil water, stir in flour, and cook until firm.", timerSec = 600),
            step("Ugali na Mayai", 2, "Beat the eggs with chopped onions."),
            step("Ugali na Mayai", 3, "Fry the egg mixture into a simple omelette and serve alongside the ugali.", timerSec = 300),

            step("Kuku Choma", 1, "Marinate the chicken with lemon, salt, and oil.", timerSec = 900),
            step("Kuku Choma", 2, "Grill over charcoal, turning occasionally, until well done and charred.", timerSec = 2400),

            step("Pilau ya Samaki", 1, "Lightly fry the fish and set aside.", timerSec = 480),
            step("Pilau ya Samaki", 2, "Caramelize the onions, then add cardamom and cinnamon.", timerSec = 600),
            step("Pilau ya Samaki", 3, "Add rice and water, simmering until the rice is nearly done.", timerSec = 1200),
            step("Pilau ya Samaki", 4, "Gently fold the fish back in and warm through before serving."),

            step("Mishkaki", 1, "Cube the beef and marinate with garlic, lemon, curry powder, and oil.", timerSec = 1800),
            step("Mishkaki", 2, "Thread onto skewers."),
            step("Mishkaki", 3, "Grill over charcoal, turning often, until charred and cooked through.", timerSec = 1200),

            step("Ndizi na Nazi", 1, "Slice the plantains."),
            step("Ndizi na Nazi", 2, "Simmer gently in coconut milk and sugar until soft and syrupy.", timerSec = 1200),

            step("Beef Tacos", 1, "Make a simple dough with flour and water, then cook thin tortillas on a dry pan.", timerSec = 600),
            step("Beef Tacos", 2, "Brown the beef with onions, peppers, and chili.", timerSec = 600),
            step("Beef Tacos", 3, "Fill the tortillas with the beef mixture and fold."),

            step("Vegetable Curry", 1, "Sauté onions and curry powder until fragrant.", timerSec = 180),
            step("Vegetable Curry", 2, "Add the chopped vegetables and stir to coat."),
            step("Vegetable Curry", 3, "Add coconut milk and simmer until the vegetables are tender.", timerSec = 1200),

            step("Fruit Salad", 1, "Chop all the fruit into bite-sized pieces."),
            step("Fruit Salad", 2, "Toss gently together and chill before serving."),

            step("Chocolate Cake", 1, "Cream the butter and sugar together until light."),
            step("Chocolate Cake", 2, "Beat in the eggs and vanilla essence."),
            step("Chocolate Cake", 3, "Fold in the flour, cocoa powder, and baking powder until just combined."),
            step("Chocolate Cake", 4, "Pour into a pan and bake until a skewer comes out clean.", timerSec = 2400),

            step("Shrimp Pasta", 1, "Boil the pasta until al dente.", timerSec = 600),
            step("Shrimp Pasta", 2, "Sauté garlic in butter until fragrant.", timerSec = 120),
            step("Shrimp Pasta", 3, "Add the prawns and cook until pink and just done.", timerSec = 300),
            step("Shrimp Pasta", 4, "Toss with the pasta and a squeeze of lemon."),

            step("Roast Chicken", 1, "Rub the chicken all over with butter, garlic, oregano, and salt."),
            step("Roast Chicken", 2, "Roast until golden and cooked through.", timerSec = 4200),

            step("Coleslaw", 1, "Shred the cabbage and carrots finely."),
            step("Coleslaw", 2, "Toss with the yogurt dressing and chill before serving."),

            step("Pumpkin Soup", 1, "Sauté onions in butter until soft.", timerSec = 240),
            step("Pumpkin Soup", 2, "Add chopped pumpkin and water, then simmer until soft.", timerSec = 1200),
            step("Pumpkin Soup", 3, "Blend until smooth and stir in the milk."),

            step("Sweet Potato Mash", 1, "Boil the sweet potatoes until soft.", timerSec = 1200),
            step("Sweet Potato Mash", 2, "Mash with butter and milk until smooth."),

            step("Beetroot Salad", 1, "Boil the beetroot until tender.", timerSec = 1200),
            step("Beetroot Salad", 2, "Slice and toss with thin onion slices and a squeeze of lime."),

            step("Hot Dog", 1, "Grill or boil the sausages until cooked through.", timerSec = 480),
            step("Hot Dog", 2, "Serve in bread with your favourite toppings."),

            step("Almond Granola", 1, "Mix oats, honey, and almonds together."),
            step("Almond Granola", 2, "Spread on a tray and bake until golden, stirring occasionally.", timerSec = 1500),

            step("Stuffed Bell Peppers", 1, "Cook the rice partway."),
            step("Stuffed Bell Peppers", 2, "Brown the beef with onions.", timerSec = 480),
            step("Stuffed Bell Peppers", 3, "Mix the beef and rice, then stuff into hollowed-out peppers."),
            step("Stuffed Bell Peppers", 4, "Top with cheese and bake until the peppers are tender.", timerSec = 1500),

            step("Mango Smoothie", 1, "Blend mango, milk, and honey until smooth."),
            step("Mango Smoothie", 2, "Serve chilled."),
        )

        db.recipeDao().insertRecipeSteps(steps)


        // ---- 5. Recipe pairings — "goes well with" suggestions ----
        fun pair(a: String, b: String): List<com.anthonykamau.foodchap.data.local.entity.RecipePairing> {
            val idA = recipeIdOf.getValue(a)
            val idB = recipeIdOf.getValue(b)
            return listOf(
                com.anthonykamau.foodchap.data.local.entity.RecipePairing(recipeId = idA, pairedRecipeId = idB),
                com.anthonykamau.foodchap.data.local.entity.RecipePairing(recipeId = idB, pairedRecipeId = idA)
            )
        }

        val pairings = mutableListOf<com.anthonykamau.foodchap.data.local.entity.RecipePairing>()
        pairings += pair("Chapati", "Ndengu")
        pairings += pair("Chapati", "Maharagwe ya Nazi")
        pairings += pair("Ugali na Sukuma Wiki", "Nyama Choma")
        pairings += pair("Ugali na Sukuma Wiki", "Managu na Nyama")
        pairings += pair("Mukimo", "Managu na Nyama")
        pairings += pair("Mukimo", "Kuku Kienyeji Fry")
        pairings += pair("Pilau", "Kachumbari")
        pairings += pair("Biryani", "Kachumbari")
        pairings += pair("Wali wa Nazi", "Samaki wa Kukaanga")
        pairings += pair("Wali wa Nazi", "Mchuzi wa Samaki")
        pairings += pair("Githeri", "Kachumbari ya Parachichi")
        pairings += pair("Irio", "Nyama Choma")
        pairings += pair("Kuku Choma", "Kachumbari")
        pairings += pair("Mishkaki", "Kachumbari")
        pairings += pair("Chapati Mayai", "Chai ya Tangawizi")
        pairings += pair("Mandazi", "Kenyan Chai")
        pairings += pair("Mahamri", "Kenyan Chai")
        pairings += pair("Spaghetti Bolognese", "Caesar Salad")
        pairings += pair("Mac and Cheese", "Coleslaw")
        pairings += pair("Roast Chicken", "Sweet Potato Mash")
        pairings += pair("Roast Chicken", "Vegetable Stir Fry")
        pairings += pair("Beef Tacos", "Chickpea Salad")
        pairings += pair("Shrimp Pasta", "Caesar Salad")
        pairings += pair("Vegetable Curry", "Wali wa Kukaanga")
        pairings += pair("Chicken Curry", "Wali wa Kukaanga")
        pairings += pair("Grilled Cheese Sandwich", "Tomato Soup")
        pairings += pair("Hot Dog", "Potato Wedges")
        pairings += pair("Banana Pancakes", "Mango Smoothie")
        pairings += pair("Classic Pancakes", "Banana Smoothie")

        db.recipeDao().insertRecipePairings(pairings)

    }
}

