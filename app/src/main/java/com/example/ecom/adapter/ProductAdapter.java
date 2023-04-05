package com.example.ecom.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecom.R;
import com.example.ecom.models.Product;
import com.example.ecom.view.fragments.ProductDetailsFragment;
import com.example.ecom.view.fragments.ProductListFragment;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<Product> productList;
    String categoryName;

    public ProductAdapter(Context context, List<Product> productList, String name) {
        this.context = context;
        this.productList = productList;
        categoryName = name;
    }

    public void addItems(List<Product> newProductList) {
        int oldSize = productList.size();
        productList.addAll(newProductList);
        notifyItemRangeInserted(oldSize, newProductList.size());
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_product_list_item,parent,false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.priceTV.setText(String.valueOf(product.getPrice()));
        holder.ratingTV.setText(String.valueOf(product.getRating().getRate()));
        Glide.with(context).load(product.getImage()).into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
            Bundle args = new Bundle();
            args.putString("productID", String.valueOf(product.getId()));
            args.putString("categoryName", categoryName);
            productDetailsFragment.setArguments(args);

            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, productDetailsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView priceTV, ratingTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            priceTV = itemView.findViewById(R.id.priceTV);
            ratingTV = itemView.findViewById(R.id.ratingTV);
        }
    }
}
