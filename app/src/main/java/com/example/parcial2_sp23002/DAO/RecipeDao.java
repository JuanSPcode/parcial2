package com.example.parcial2_sp23002.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.parcial2_sp23002.Entity.Ingredient;
import com.example.parcial2_sp23002.Entity.Recipe;

import java.util.List;

@Dao
public abstract class RecipeDao {
    @Insert
    public abstract long insertRecipe(Recipe recipe);

    @Update
    public abstract void updateRecipe(Recipe recipe);

    @Delete
    public abstract void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes")
    public abstract List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE idRecipe = :id")
    public abstract Recipe getRecipeById(int id);

    @Transaction
    public void insertRecipeWithIngredients(Recipe recipe, List<Ingredient> ingredients, IngredientDao ingredientDao) {
        long recipeId = insertRecipe(recipe);
        for (Ingredient ingredient : ingredients) {
            ingredient.recipeId = (int) recipeId;
            ingredientDao.insertIngredient(ingredient);
        }
    }
}
