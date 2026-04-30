package com.example.parcial2_sp23002.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.parcial2_sp23002.Entity.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insertRecipe(Recipe recipe);

    @Update
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE idRecipe = :id")
    Recipe getRecipeById(int id);
}
