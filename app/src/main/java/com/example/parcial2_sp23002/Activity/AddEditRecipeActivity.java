package com.example.parcial2_sp23002.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial2_sp23002.AppDatabase;
import com.example.parcial2_sp23002.Entity.Ingredient;
import com.example.parcial2_sp23002.Entity.Recipe;
import com.example.parcial2_sp23002.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddEditRecipeActivity extends AppCompatActivity {

    private TextInputEditText etName, etDescription, etServings;
    private LinearLayout ingredientsContainer;
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
        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        Button btnAddIngredientField = findViewById(R.id.btnAddIngredientField);
        Button btnSave = findViewById(R.id.btnSave);

        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            setTitle("Editar Receta");
            Recipe recipe = db.recipeDao().getRecipeById(recipeId);
            if (recipe != null) {
                etName.setText(recipe.name);
                etDescription.setText(recipe.description);
                etServings.setText(String.valueOf(recipe.servings));
                
                // Cargar ingredientes existentes
                List<Ingredient> ingredients = db.ingredientDao().getIngredientsForRecipe(recipeId);
                for (Ingredient ing : ingredients) {
                    addIngredientRow(ing);
                }
            }
        } else {
            setTitle("Nueva Receta");
            addIngredientRow(null); // Al menos un ingrediente obligatorio
        }

        btnAddIngredientField.setOnClickListener(v -> addIngredientRow(null));
        btnSave.setOnClickListener(v -> saveRecipe());
    }

    private void addIngredientRow(Ingredient ingredient) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_ingredient_row, ingredientsContainer, false);
        TextInputEditText etIngName = row.findViewById(R.id.etIngredientName);
        TextInputEditText etIngQty = row.findViewById(R.id.etIngredientQuantity);
        
        if (ingredient != null) {
            etIngName.setText(ingredient.name);
            etIngQty.setText(ingredient.quantity);
            // Guardamos el ID en el tag para saber que es uno existente si fuera necesario, 
            // pero para simplificar, el guardado borrará y reinsertará o actualizará.
            row.setTag(ingredient.idIngredient);
        }

        View btnRemove = row.findViewById(R.id.btnRemoveRow);
        btnRemove.setOnClickListener(v -> {
            if (ingredientsContainer.getChildCount() > 1) {
                ingredientsContainer.removeView(row);
            } else {
                Toast.makeText(this, "Debe haber al menos un ingrediente", Toast.LENGTH_SHORT).show();
            }
        });
        ingredientsContainer.addView(row);
    }

    private void saveRecipe() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String servingsStr = etServings.getText().toString().trim();

        if (name.isEmpty() || servingsStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int servings;
        try {
            servings = Integer.parseInt(servingsStr);
            if (servings <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Porciones debe ser un número entero positivo", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Ingredient> ingredientsToSave = new ArrayList<>();
        for (int i = 0; i < ingredientsContainer.getChildCount(); i++) {
            View row = ingredientsContainer.getChildAt(i);
            TextInputEditText etIngName = row.findViewById(R.id.etIngredientName);
            TextInputEditText etIngQty = row.findViewById(R.id.etIngredientQuantity);

            String iName = etIngName.getText().toString().trim();
            String iQty = etIngQty.getText().toString().trim();

            if (iName.isEmpty() || iQty.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos de ingredientes", Toast.LENGTH_SHORT).show();
                return;
            }
            ingredientsToSave.add(new Ingredient(recipeId != -1 ? recipeId : 0, iName, iQty));
        }

        if (ingredientsToSave.isEmpty()) {
            Toast.makeText(this, "Debe agregar al menos un ingrediente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (recipeId == -1) {
            // Uso de Transacción para insertar Receta + Ingredientes
            db.recipeDao().insertRecipeWithIngredients(new Recipe(name, desc, servings), ingredientsToSave, db.ingredientDao());
            Toast.makeText(this, "Receta e ingredientes guardados", Toast.LENGTH_SHORT).show();
        } else {
            // Actualizar receta
            Recipe recipe = db.recipeDao().getRecipeById(recipeId);
            if (recipe != null) {
                recipe.name = name;
                recipe.description = desc;
                recipe.servings = servings;
                db.recipeDao().updateRecipe(recipe);
                
                // Para simplificar la edición de ingredientes embebidos:
                // 1. Borramos los anteriores
                List<Ingredient> oldIngredients = db.ingredientDao().getIngredientsForRecipe(recipeId);
                for(Ingredient old : oldIngredients) db.ingredientDao().eliminar(old.idIngredient);
                
                // 2. Insertamos los nuevos del formulario
                for (Ingredient ing : ingredientsToSave) {
                    ing.recipeId = recipeId;
                    db.ingredientDao().insertIngredient(ing);
                }

                Toast.makeText(this, "Receta actualizada", Toast.LENGTH_SHORT).show();
            }
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}
