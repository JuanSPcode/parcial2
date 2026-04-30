package com.example.parcial2_sp23002.DAO;

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
    List<Ingredient> getAllIngredients();

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    List<Ingredient> getIngredientsForRecipe(int recipeId);

    @Query("SELECT * FROM ingredients WHERE idIngredient = :id")
    Ingredient getIngredientById(int id);

    // Requerimiento: Mostrar a qué receta pertenece el ingrediente
    @Query("SELECT ingredients.*, recipes.name as recipeName FROM ingredients " +
           "INNER JOIN recipes ON ingredients.recipeId = recipes.idRecipe")
    List<IngredientWithRecipe> getAllIngredientsWithRecipeName();

    @Query("SELECT ingredients.*, recipes.name as recipeName FROM ingredients " +
           "INNER JOIN recipes ON ingredients.recipeId = recipes.idRecipe " +
           "WHERE ingredients.recipeId = :recipeId")
    List<IngredientWithRecipe> getIngredientsForRecipeWithRecipeName(int recipeId);

    class IngredientWithRecipe {
        public int idIngredient;
        public int recipeId;
        public String name;
        public String quantity;
        public String recipeName;
    }
}
