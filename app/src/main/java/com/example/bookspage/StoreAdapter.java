package com.example.bookspage; // Make sure this matches your package name

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> storeList;

    public StoreAdapter(List<Store> storeList) {
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_store.xml layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store currentStore = storeList.get(position);
        holder.storeNameTextView.setText(currentStore.getName());
        holder.storeImageView.setImageResource(currentStore.getImageResId());
        // You can also set click listeners here if you want to handle clicks on individual items
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    // ViewHolder class to hold references to the views in item_store.xml
    static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storeImageView;
        TextView storeNameTextView;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImageView = itemView.findViewById(R.id.storeImageView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
        }
    }
}
