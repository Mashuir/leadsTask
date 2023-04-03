package com.example.ecom.view.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ecom.adapter.CategoryAdapter;
import com.example.ecom.databinding.FragmentShopBinding;
import com.example.ecom.networks.ApiClient;
import com.example.ecom.networks.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends Fragment {

    FragmentShopBinding binding;
    ApiService apiService;
    CategoryAdapter categoryAdapter;

    public ShopFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.categoryRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
        binding.categoryRecyclerView.setHasFixedSize(true);


        apiService = ApiClient.getClient();
        apiService.getCategories().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {

                List<String> categoryArray = response.body();
                categoryAdapter = new CategoryAdapter(requireContext(),categoryArray);
                binding.categoryRecyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {

            }
        });
    }
}