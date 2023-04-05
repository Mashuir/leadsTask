package com.example.ecom.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.ecom.R;
import com.example.ecom.models.Product;
import com.example.ecom.networks.ApiClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> IDs = sharedPreferences.getStringSet("Items", new HashSet<>());

        List<Call<Product>> calls = new ArrayList<>();

        for (String id : IDs) {
            calls.add(ApiClient.getClient().getProductDetails(id));
            Log.d("idesmksmk",id);
        }

        for (Call<Product> call : calls) {
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product product = response.body();
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    // handle the failure
                }
            });
        }


    }
}