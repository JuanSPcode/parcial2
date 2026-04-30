package com.example.parcial2_sp23002.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial2_sp23002.Entity.Ingredient;
import com.example.parcial2_sp23002.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingrediente;
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onEditClick(Ingredient ingrediente);
        void onDeleteClick(Ingredient ingrediente);
    }

    public IngredientAdapter(List<Ingredient> ingredients, OnIngredientClickListener listener) {
        this.ingrediente = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingrediente.get(position);
        holder.txtNombre.setText(ingredient.name);
        holder.txtCantidad.setText(ingredient.quantity);
        holder.txtRecipeRef.setText("Receta ID: " + ingredient.recipeId);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(ingredient));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingrediente.size();
    }

    public void setIngrediente(List<Ingredient> ingrediente) {
        this.ingrediente = ingrediente;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCantidad, txtRecipeRef;
        ImageButton btnEdit, btnDelete;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.tvIngredientName);
            txtCantidad = itemView.findViewById(R.id.tvIngredientQuantity);
            txtRecipeRef = itemView.findViewById(R.id.tvRecipeReference);
            btnEdit = itemView.findViewById(R.id.btnEditIngredient);
            btnDelete = itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}
