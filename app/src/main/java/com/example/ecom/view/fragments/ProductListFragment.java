package com.example.ecom.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecom.IOnBackPressed;
import com.example.ecom.R;
import com.example.ecom.adapter.ProductAdapter;
import com.example.ecom.databinding.FragmentProductListBinding;
import com.example.ecom.databinding.FragmentShopBinding;
import com.example.ecom.models.Product;
import com.example.ecom.networks.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment implements IOnBackPressed {

    FragmentProductListBinding binding;
    String categoryName;
    private int offset = 0;
    private boolean isLoading = false;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            categoryName = args.getString("categoryName");
        }

        binding.productListRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
        binding.productListRecyclerView.setHasFixedSize(true);

        loadMoreData();

        binding.productListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                    loadMoreData();
                }
            }
        });
    }

    private void loadMoreData() {

        isLoading = true;

        Call<List<Product>> call = ApiClient.getClient().getProductListByCategory(categoryName, 10, offset);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                List<Product> productList = response.body();

                if (productList != null) {
                    if (offset == 0) {
                        ProductAdapter productAdapter = new ProductAdapter(requireContext(), productList,categoryName);
                        binding.productListRecyclerView.setAdapter(productAdapter);
                    } else {
                        ProductAdapter productAdapter = (ProductAdapter) binding.productListRecyclerView.getAdapter();
                        assert productAdapter != null;
                        productAdapter.addItems(productList);
                    }

                    offset += productList.size();
                }

                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                isLoading = false;
                // handle the failure
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        //getActivity().finish();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new ShopFragment()).commit();
        BottomNavigationView navigationView = requireActivity().findViewById(R.id.bottomNavigation);
        navigationView.getMenu().getItem(0).setChecked(true);
        return true;
    }
}