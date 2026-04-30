package com.example.parcial2_sp23002.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients",
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = "idRecipe",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("recipeId")})
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public int idIngredient;
    public int recipeId;
    public String name;
    public String quantity;

    public Ingredient(int recipeId, String name, String quantity) {
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
    }
}
