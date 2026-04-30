package com.example.parcial2_sp23002.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial2_sp23002.Adapter.IngredientAdapter;
import com.example.parcial2_sp23002.AppDatabase;
import com.example.parcial2_sp23002.Entity.Ingredient;
import com.example.parcial2_sp23002.R;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity implements IngredientAdapter.OnIngredientClickListener {

    private AppDatabase db;
    private IngredientAdapter adapter;
    private int recipeId;
    private String recipeNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        recipeNombre = getIntent().getStringExtra("RECIPE_NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ingredientes: " + recipeNombre);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);
        RecyclerView rvIngredients = findViewById(R.id.rvIngredients);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));

        adapter = new IngredientAdapter(new ArrayList<>(), this);
        rvIngredients.setAdapter(adapter);

        db.ingredientDao().getIngredientsForRecipe(recipeId).observe(this, ingredients -> {
            adapter.setIngrediente(ingredients);
        });
    }

    @Override
    public void onEditClick(Ingredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Cantidad");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_quantity, null);
        builder.setView(view);

        TextInputEditText etQuantity = view.findViewById(R.id.etEditQuantity);
        etQuantity.setText(ingredient.quantity);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            String newQty = etQuantity.getText().toString().trim();
            if (!newQty.isEmpty()) {
                ingredient.quantity = newQty;
                db.ingredientDao().actualizar(ingredient);
                Toast.makeText(this, "Cantidad actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "La cantidad no puede estar vacía", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onDeleteClick(Ingredient ingredient) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Ingrediente")
                .setMessage("¿Estás seguro de eliminar \"" + ingredient.name + "\"?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    db.ingredientDao().eliminar(ingredient.idIngredient);
                    Toast.makeText(this, "Ingrediente eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
