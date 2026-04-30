package com.example.parcial2_sp23002.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int idRecipe;
    public String name;
    public String description;
    public int servings;

    public Recipe(String name, String description, int servings) {
        this.name = name;
        this.description = description;
        this.servings = servings;
    }

    @NonNull
    @Override
    public String toString() {
        return name != null ? name : "";
    }
}
