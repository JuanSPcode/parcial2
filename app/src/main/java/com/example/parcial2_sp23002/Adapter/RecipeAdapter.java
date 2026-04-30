package com.example.parcial2_sp23002.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial2_sp23002.Entity.Recipe;
import com.example.parcial2_sp23002.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {

    private List<Recipe> recipes;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
        void onEditClick(Recipe recipe);
        void onDeleteClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temp, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.txtNombre.setText(recipe.name);
        holder.txtDescripcion.setText(recipe.description);
        holder.txtPorciones.setText("Porciones: " + recipe.servings);

        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
        holder.btnVer.setOnClickListener(v -> listener.onRecipeClick(recipe));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(recipe));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void actualizar(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDescripcion, txtPorciones;
        Button btnEdit, btnDelete, btnVer;

        public VH(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.nombreRecipeItem);
            txtDescripcion = itemView.findViewById(R.id.descripcionRecipeItem);
            txtPorciones = itemView.findViewById(R.id.porcionesRecipeItem);
            btnEdit = itemView.findViewById(R.id.editarRecipeItem);
            btnDelete = itemView.findViewById(R.id.eliminarRecipeItem);
            btnVer = itemView.findViewById(R.id.verIngrediente);
        }
    }
}
