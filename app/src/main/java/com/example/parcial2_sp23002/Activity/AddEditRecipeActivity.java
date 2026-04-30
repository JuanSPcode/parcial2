package com.example.parcial2_sp23002.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2_sp23002.AppDatabase;
import com.example.parcial2_sp23002.Entity.Recipe;
import com.example.parcial2_sp23002.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditRecipeActivity extends AppCompatActivity {

    private TextInputEditText etName, etDescription, etServings;
    private AppDatabase db;
    private int recipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        db = AppDatabase.getInstance(this);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etServings = findViewById(R.id.etServings);
        Button btnSave = findViewById(R.id.btnSave);

        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            setTitle("Editar Receta");
            Recipe recipe = db.recipeDao().getRecipeById(recipeId);
            if (recipe != null) {
                etName.setText(recipe.name);
                etDescription.setText(recipe.description);
                etServings.setText(String.valueOf(recipe.servings));
            }
        } else {
            setTitle("Nueva Receta");
        }

        btnSave.setOnClickListener(v -> saveRecipe());
    }

    private void saveRecipe() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String servingsStr = etServings.getText().toString().trim();

        if (name.isEmpty() || servingsStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int servings = Integer.parseInt(servingsStr);
        if (servings <= 0) {
            Toast.makeText(this, "Porciones debe ser un número positivo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (recipeId == -1) {
            db.recipeDao().insertRecipe(new Recipe(name, desc, servings));
            Toast.makeText(this, "Receta guardada", Toast.LENGTH_SHORT).show();
        } else {
            Recipe recipe = db.recipeDao().getRecipeById(recipeId);
            if (recipe != null) {
                recipe.name = name;
                recipe.description = desc;
                recipe.servings = servings;
                db.recipeDao().updateRecipe(recipe);
                Toast.makeText(this, "Receta actualizada", Toast.LENGTH_SHORT).show();
            }
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}
