# 🍲 FoodChap

**FoodChap** is a comprehensive Kenyan meal-planning Android application designed to simplify the cooking experience. Built with a focus on local ingredients, real market pricing, and intuitive planning, it helps users move from "What's for dinner?" to a fully costed shopping list in minutes.

---

## ✨ Key Features

### 📖 Recipe Discovery
*   **90+ Curated Recipes:** A diverse collection of both traditional Kenyan staples (Ugali, Pilau, Mukimo) and popular global dishes.
*   **Smart Filtering:** Quickly browse by category—Breakfast, Main Meals, or Snacks—or use the real-time search bar to find exactly what you're looking for.
*   **Interactive Instructions:** Detailed, step-by-step guides for every recipe, featuring built-in **cooking timers** to ensure perfect results every time.
*   **Smart Pairings:** "Goes well with" suggestions help you build a complete meal (e.g., pairing Nyama Choma with Kachumbari).

### 🥕 "What Can I Cook?"
*   **Inventory-Based Suggestions:** Input the ingredients you already have, and the app calculates which recipes you can make.
*   **Yield Estimation:** Get an estimate of how many servings you can prepare based on your current stock.

### 📅 Weekly Meal Planner
*   **Personalized Schedules:** Organize your entire week by assigning recipes to specific days.
*   **Easy Management:** Seamlessly add recipes to your planner directly from the detail view.

### 🛒 Automated Shopping List
*   **Dynamic Totals:** Your meal plan automatically generates a consolidated shopping list.
*   **Real Kenyan Pricing:** Costs are estimated using mid-2024 Kenyan market rates (KES), helping you stay within budget.
*   **Smart Grouping:** Ingredients are categorized (Staples, Protein, Vegetables, etc.) for a faster supermarket experience.

### 🎨 Personalization & UX
*   **🌓 Dark Mode Support:** A beautiful, eye-pleasing theme toggleable within the app.
*   **🚀 Seamless Onboarding:** A friendly walkthrough for first-time users to explore the app's core value.

---

## 🛠 Tech Stack & Architecture

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Framework:** Native Android (XML / View-based)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Local Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room)
*   **Reactive UI:** LiveData & ViewModel
*   **View Binding:** For safe and efficient layout interaction
*   **Dependency Management:** Gradle (Kotlin DSL)

---

## 🚀 Getting Started

### Prerequisites
*   Android Studio Ladybug (or newer)
*   Android SDK Level 34+
*   Java 17

### Installation
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/anthony-kamau/FoodChap.git
    ```
2.  **Open in Android Studio:**
    Select `File > Open` and navigate to the project root.
3.  **Sync Gradle:**
    Allow Android Studio to download dependencies and sync the project structure.
4.  **Run the App:**
    Connect a device or emulator and click the **Run** button.

---

## 🏗 Project Structure
*   `ui/`: Contains Activities, ViewModels, and Adapters organized by feature (browse, detail, planner, shopping).
*   `data/`: Handles local persistence via Room (DAOs, Entities, and the AppDatabase).
*   `data/seed/`: Contains `SeedData.kt` which populates the local database with the master ingredient catalog and recipes on the first launch.

---

## 👨‍💻 Author
**Anthony Kamau**
*   GitHub: [@anthony-kamau](https://github.com/anthony-kamau)
*   Watch the app walkthrough on Youtube (https://youtu.be/Yzu__34wLhI)

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
