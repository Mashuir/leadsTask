package com.example.ecom.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom.R;
import com.example.ecom.models.Category;
import com.example.ecom.view.fragments.ProductListFragment;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    List<String> categoryList;

    public CategoryAdapter(Context context, List<String> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_list_item,parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {

        String category_NAME = categoryList.get(position);

        holder.categoryName.setText(category_NAME);

        holder.itemView.setOnClickListener(v -> {

            ProductListFragment productListFragment = new ProductListFragment();
            Bundle args = new Bundle();
            args.putString("categoryName", category_NAME);
            productListFragment.setArguments(args);

            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, productListFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}
