package com.example.ecom.view.fragments;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment implements IOnBackPressed {

    FragmentProductDetailsBinding binding;
    String categoryID;

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
            categoryID = args.getString("categoryID");
        }

        ApiClient.getClient().getProductDetails(categoryID).enqueue(new Callback<Product>() {
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
    }

    @Override
    public boolean onBackPressed() {
        //getActivity().finish();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new ProductListFragment()).commit();
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigation);
        navigationView.getMenu().getItem(0).setChecked(true);
        return true;
    }
}