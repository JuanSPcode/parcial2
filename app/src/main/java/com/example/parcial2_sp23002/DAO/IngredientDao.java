package com.example.parcial2_sp23002.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.parcial2_sp23002.Entity.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insertIngredient(Ingredient ingredient);

    @Insert
    void insertar(List<Ingredient> ingredients);

    @Update
    void actualizar(Ingredient ingredient);

    @Query("DELETE FROM ingredients WHERE idIngredient = :id")
    void eliminar(int id);

    @Query("SELECT * FROM ingredients")
    LiveData<List<Ingredient>> getAllIngredients();

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeId);

    @Query("SELECT * FROM ingredients WHERE idIngredient = :id")
    Ingredient getIngredientById(int id);
}
