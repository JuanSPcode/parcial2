package com.example.parcial2_sp23002;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.parcial2_sp23002.DAO.IngredientDao;
import com.example.parcial2_sp23002.DAO.RecipeDao;
import com.example.parcial2_sp23002.Entity.Ingredient;
import com.example.parcial2_sp23002.Entity.Recipe;

@Database(entities = {Recipe.class, Ingredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "db_parcial")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
