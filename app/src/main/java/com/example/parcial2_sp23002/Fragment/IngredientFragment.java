package com.example.parcial2_sp23002.Fragment;

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

import com.example.parcial2_sp23002.Activity.AddEditIngredientActivity;
import com.example.parcial2_sp23002.Adapter.IngredientAdapter;
import com.example.parcial2_sp23002.AppDatabase;
import com.example.parcial2_sp23002.Entity.Ingredient;
import com.example.parcial2_sp23002.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class IngredientFragment extends Fragment implements IngredientAdapter.OnIngredientClickListener {

    private AppDatabase db;
    private IngredientAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        db = AppDatabase.getInstance(requireContext());
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new IngredientAdapter(new ArrayList<>(), this);
        rv.setAdapter(adapter);

        db.ingredientDao().getAllIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            adapter.setIngrediente(ingredients);
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                }
        );

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
            launcher.launch(intent);
        });

        return view;
    }

    @Override
    public void onEditClick(Ingredient ingredient) {
        Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
        intent.putExtra("INGREDIENT_ID", ingredient.idIngredient);
        launcher.launch(intent);
    }

    @Override
    public void onDeleteClick(Ingredient ingredient) {



        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar")
                .setMessage("Esta seguro de eliminar ->" + ingredient.name + "?")
                .setPositiveButton("SI", (dialog, which) -> {
                    db.ingredientDao().eliminar(ingredient.idIngredient);
                    Toast.makeText(getContext(), "Ingrediente eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    Toast.makeText(getContext(), "Accion cancelada", Toast.LENGTH_SHORT).show();
                }).show();
    }
}

