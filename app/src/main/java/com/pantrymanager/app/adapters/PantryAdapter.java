package com.pantrymanager.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pantrymanager.app.R;
import com.pantrymanager.app.models.Ingredient;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<Ingredient> pantryList;
    private OnItemClickListener listener;

    // Must define this interface so PantryActivity can handle clicks
    public interface OnItemClickListener {
        void onEditClick(Ingredient ingredient);
        void onDeleteClick(Ingredient ingredient);
    }

    public PantryAdapter(List<Ingredient> pantryList, OnItemClickListener listener) {
        this.pantryList = pantryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry_row, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        Ingredient currentItem = pantryList.get(position);

        holder.tvName.setText(currentItem.getName());
        holder.tvQuantity.setText("Qty: " + currentItem.getQuantity() + " " + currentItem.getUnit());

        if (currentItem.getExpiryDate() != null && !currentItem.getExpiryDate().isEmpty()) {
            holder.tvExpiry.setText("Expires: " + currentItem.getExpiryDate());
        } else {
            holder.tvExpiry.setText("No expiry date");
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(currentItem));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(currentItem));
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    public void updateList(List<Ingredient> newList) {
        pantryList = newList;
        notifyDataSetChanged();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiry;
        ImageButton btnEdit, btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ingredient_name);
            tvQuantity = itemView.findViewById(R.id.tv_ingredient_quantity);
            tvExpiry = itemView.findViewById(R.id.tv_ingredient_expiry);
            btnEdit = itemView.findViewById(R.id.btn_edit_pantry);
            btnDelete = itemView.findViewById(R.id.btn_delete_pantry);
        }
    }
}