package com.example.ecom.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecom.R;
import com.example.ecom.models.Product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<Product> productList;

    public CartAdapter(Context context, List<Product> product) {
        this.context = context;
        this.productList = product;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cart_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.productTitle.setText(product.getTitle());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        Glide.with(context).load(product.getImage()).into(holder.cartProductImage);

        holder.cartBookDelete.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            Set<String> originalIDs = sharedPreferences.getStringSet("Items", new HashSet<>());


            Set<String> updatedIDs = new HashSet<>(originalIDs);
            String idToRemove = String.valueOf(productList.get(holder.getAdapterPosition()).getId());
            updatedIDs.remove(idToRemove);


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("Items", updatedIDs);
            editor.apply();


            productList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cartProductImage, cartBookDelete;
        TextView productTitle, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            cartBookDelete = itemView.findViewById(R.id.cartBookDelete);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
