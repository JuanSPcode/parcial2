package com.example.parcial2_sp23002.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2_sp23002.AppDatabase;
import com.example.parcial2_sp23002.Entity.Ingredient;
import com.example.parcial2_sp23002.Entity.Recipe;
import com.example.parcial2_sp23002.R;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class AddEditIngredientActivity extends AppCompatActivity {

    private TextInputEditText etName, etQuantity;
    private Spinner spRecipes;
    private AppDatabase db;
    private int ingredientId = -1;
    private List<Recipe> recipeList = new ArrayList<>();
    private ArrayAdapter<Recipe> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        db = AppDatabase.getInstance(this);
        etName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etIngredientQuantity);
        spRecipes = findViewById(R.id.spRecipes);
        Button btnSave = findViewById(R.id.btnSaveIngredient);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recipeList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRecipes.setAdapter(spinnerAdapter);

        ingredientId = getIntent().getIntExtra("INGREDIENT_ID", -1);
        int initialRecipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        db.recipeDao().getAllRecipes().observe(this, recipes -> {
            recipeList.clear();
            recipeList.addAll(recipes);
            spinnerAdapter.notifyDataSetChanged();

            if (ingredientId != -1) {
                Ingredient ingredient = db.ingredientDao().getIngredientById(ingredientId);
                if (ingredient != null) {
                    setSpinnerSelection(ingredient.recipeId);
                }
            } else if (initialRecipeId != -1) {
                setSpinnerSelection(initialRecipeId);
            }
        });

        if (ingredientId != -1) {
            setTitle("Editar Ingrediente");
            Ingredient ingredient = db.ingredientDao().getIngredientById(ingredientId);
            if (ingredient != null) {
                etName.setText(ingredient.name);
                etQuantity.setText(ingredient.quantity);
            }
        } else {
            setTitle("Nuevo Ingrediente");
        }

        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private void setSpinnerSelection(int recipeId) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).idRecipe == recipeId) {
                spRecipes.setSelection(i);
                break;
            }
        }
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String qty = etQuantity.getText().toString().trim();
        Recipe selectedRecipe = (Recipe) spRecipes.getSelectedItem();

        if (name.isEmpty() || qty.isEmpty() || selectedRecipe == null) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int recipeId = selectedRecipe.idRecipe;

        if (ingredientId == -1) {
            db.ingredientDao().insertIngredient(new Ingredient(recipeId, name, qty));
            Toast.makeText(this, "Ingrediente guardado", Toast.LENGTH_SHORT).show();
        } else {
            Ingredient ingredient = db.ingredientDao().getIngredientById(ingredientId);
            if (ingredient != null) {
                ingredient.name = name;
                ingredient.quantity = qty;
                ingredient.recipeId = recipeId;
                db.ingredientDao().actualizar(ingredient);
                Toast.makeText(this, "Ingrediente actualizado", Toast.LENGTH_SHORT).show();
            }
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}
