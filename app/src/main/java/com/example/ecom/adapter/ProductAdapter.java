package com.example.ecom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecom.R;
import com.example.ecom.models.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
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
