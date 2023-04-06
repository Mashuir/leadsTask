package com.example.ecom.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.ecom.OnAddToCartListener;
import com.example.ecom.adapter.CartAdapter;
import com.example.ecom.databinding.ActivityCartBinding;
import com.example.ecom.models.Product;
import com.example.ecom.networks.ApiClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements OnAddToCartListener{

    ActivityCartBinding binding;
    private OnAddToCartListener onAddToCartListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> IDs = sharedPreferences.getStringSet("Items", new HashSet<>());

        List<Call<Product>> calls = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        for (String id : IDs) {
            calls.add(ApiClient.getClient().getProductDetails(id));
        }

        for (Call<Product> call : calls) {
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                    // handle the response
                    Product product = response.body();
                    productList.add(product);

                    if (productList.size() == calls.size()) {
                        // all products have been loaded, create the adapter
                        onAddToCartListener = getIntent().getParcelableExtra("onAddToCartListener");
                        CartAdapter adapter = new CartAdapter(CartActivity.this, productList, onAddToCartListener);
                        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        binding.cartRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                    // handle the failure
                }
            });
        }



    }

    @Override
    public void onAddToCart() {
        invalidateOptionsMenu();
    }
}