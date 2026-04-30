package com.example.parcial2_sp23002.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial2_sp23002.Activity.AddEditRecipeActivity;
import com.example.parcial2_sp23002.Adapter.RecipeAdapter;
import com.example.parcial2_sp23002.AppDatabase;
import com.example.parcial2_sp23002.Entity.Recipe;
import com.example.parcial2_sp23002.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private AppDatabase db;
    private RecipeAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        db = AppDatabase.getInstance(requireContext());
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecipeAdapter(new ArrayList<>(), this);
        rv.setAdapter(adapter);

        db.recipeDao().getAllRecipes().observe(getViewLifecycleOwner(), recipes -> {
            adapter.actualizar(recipes);
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // LiveData updates automatically
                    }
                }
        );

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditRecipeActivity.class);
            launcher.launch(intent);
        });

        return view;
    }

    @Override
    public void onRecipeClick(Recipe recipe) {

    }

    @Override
    public void onEditClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), AddEditRecipeActivity.class);
        intent.putExtra("RECIPE_ID", recipe.idRecipe);
        launcher.launch(intent);
    }

    @Override
    public void onDeleteClick(Recipe recipe) {

        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar")
                .setMessage("Esta seguro de eliminar ->" + recipe.name + "?")
                .setPositiveButton("SI", (dialog, which) -> {
                    db.recipeDao().deleteRecipe(recipe);
                    Toast.makeText(getContext(), "Receta eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    Toast.makeText(getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                }).show();
        }

    }
