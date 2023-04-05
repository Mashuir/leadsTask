package com.example.ecom.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.ecom.IOnBackPressed;
import com.example.ecom.R;
import com.example.ecom.databinding.FragmentProductDetailsBinding;
import com.example.ecom.databinding.FragmentProductListBinding;
import com.example.ecom.models.Product;
import com.example.ecom.networks.ApiClient;
import com.example.ecom.networks.ApiService;
import com.example.ecom.view.activity.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment implements IOnBackPressed {

    FragmentProductDetailsBinding binding;
    String productID, categoryName;

    public ProductDetailsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            productID = args.getString("productID");
            categoryName = args.getString("categoryName");
        }

        ApiClient.getClient().getProductDetails(productID).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {

                Product product = response.body();

                assert product != null;
                binding.productName.setText(product.getTitle());
                binding.productPrice.setText(String.valueOf(product.getPrice()));
                binding.productRating.setText(String.valueOf(product.getRating().getRate()));
                binding.productRatingCount.setText(String.valueOf(product.getRating().getCount()));
                binding.Details.setText(product.getDescription());
                Glide.with(requireContext()).load(product.getImage()).into(binding.productImage);
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {

            }
        });

        binding.addToCartButton.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Set<String> IDs = new HashSet<>(sharedPreferences.getStringSet("Items", new HashSet<>()));
            IDs.add(productID);
            editor.putStringSet("Items", IDs);
            editor.apply();
            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            mainActivity.SharedPreferencesDataUpdate();
            Snackbar.make(binding.productDetailsRootLayout,"Add to Cart", BaseTransientBottomBar.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onBackPressed() {

        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString("categoryName", categoryName);
        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigation);
        navigationView.getMenu().getItem(0).setChecked(true);
        return true;
    }
}